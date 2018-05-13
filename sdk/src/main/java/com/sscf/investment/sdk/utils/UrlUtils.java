package com.sscf.investment.sdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class UrlUtils {

    // yorkhuang 2012-03-02
    // 重写了新的较为严格的正则表达式，为了解决若干中文以英文"."分隔开的非法URL被判断成合法，没有转SOSO而导致传输给服务器域名解析出错的问题
    private static Pattern mVALIDURL = Pattern
            .compile(
                    "((http://)?(\\w+[.])*|(www.))\\w+[.]"
                            + "([a-z]{2,4})?[[.]([a-z]{2,4})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)"
                            + "?([.][a-z]{2,4}+|/?)", Pattern.CASE_INSENSITIVE);
    private static Pattern mVALIDLOCALURL = Pattern.compile(
            "(.+)localhost(:)?(\\d)*/(.+)(\\.)(.+)", Pattern.CASE_INSENSITIVE);
    private static Pattern mVALIDMTTURL = Pattern.compile("mtt://(.+)",
            Pattern.CASE_INSENSITIVE);
    private static Pattern mVALIDQBEURL = Pattern.compile("qube://(.+)",
            Pattern.CASE_INSENSITIVE);
    private static Pattern mVALIDIPADDRESS = Pattern.compile(
            "(\\d){1,3}\\.(\\d){1,3}"
                    + "\\.(\\d){1,3}\\.(\\d){1,3}(:\\d{1,4})?(/(.*))?",
            Pattern.CASE_INSENSITIVE);

    private static final String URL_ENCODING = "utf-8";

    /** URL合法字母 */
    private static Pattern mVALIDURLLETTER = Pattern
            .compile("^[-a-zA-Z0-9.#%@:/?&~=_!]+$");

    private static final String PROTOCOL_SEPARATOR = "://";

    public static final String LOCAL_FILE_PREFIX = "file://";

    private UrlUtils() {
    }

    /**
     * url 合法性预处理 过滤非法字符/替换空格/编码escape
     * 
     * @param url
     * @return
     */
    public static String prepareUrl(String url) {
        if (url == null || url.length() == 0) {
            return url;
        }

        // 1.页面内跳转
        if (url.charAt(0) == '#') {
            return url;
        }

        // 2.处理非法字符如回车、把空格替换为20%

        // 添加try{}catch(){}，BUG7068961，levijiang 2011-05-29
        try {
            url = url.replaceAll(" ", "%20");
            url = url.replaceAll("&amp;", "&");
            url = url.replaceAll("\\|", "%7C");
            url = url.replaceAll("\\^", "%5E");
            // url = url.replaceAll("#", "%23");
            // url = url.replaceAll("%2F", "/");
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }

        if (!isSmsUrl(url)) {
            // 3. url escape
            // url = UrlUtility.escape(url);
            url = escapeAllChineseChar(url);
        }

        return url;
    }

    /**
     * string url to URL
     * 
     * @param url
     * @return
     */
    public static URL toURL(String url) throws MalformedURLException {
        URL uRL = new URL(url);

        // 有个别 URL 在 path 和 querystring 之间缺少 / 符号，需补上
        if (uRL.getPath() == null || "".equals(uRL.getPath())) {
            if (uRL.getFile() != null && uRL.getFile().startsWith("?")) {
                // 补斜杠符号
                int idx = url.indexOf('?');
                if (idx != -1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(url.substring(0, idx));
                    sb.append('/');
                    sb.append(url.substring(idx));

                    uRL = new URL(sb.toString());

                    // System.out.println("toURL : " + _URL.toString());
                }
            }

            // 分支走到这里，没有path也没有file，证明为一个没有/的host，例如:
            // http://m.cnbeta.com(注意：后面没有/)
            if (uRL.getFile() == null || "".equals(uRL.getFile())) {
                StringBuilder sb = new StringBuilder();
                sb.append(url);
                sb.append("/");

                uRL = new URL(sb.toString());
            }

        }
        return uRL;
    }

    public static String escapeAllChineseChar(String url) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);

            if (c >= 0x4E00 && c <= 0x9FFF || c >= 0xFE30 && c <= 0xFFA0) {
                String escapedStr;
                try {
                    escapedStr = URLEncoder.encode(String.valueOf(c),
                            URL_ENCODING);
                    DtLog.d("Url", " : " + escapedStr);
                    sb.append(escapedStr);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                sb.append(c);
            }
        }
        // Log.d("Url", " dest : " + sb.toString());
        return sb.toString();
    }

    /**
     * url escape
     * 
     * @param url
     * @return
     */
    public static String escape(String url) {
        if (url == null || url.trim().length() == 0) {
            return url;
        }

        try {
            int idx = url.indexOf('?');
            if (idx != -1) {
                String path = url.substring(0, idx + 1);
                String queryString = url.substring(idx + 1);

                String[] nameValues = queryString.split("&");

                StringBuilder sb = new StringBuilder();
                sb.append(path);

                boolean hasParam = false;

                for (String nameValue : nameValues) {
                    // System.out.println("nameValue : " + nameValue);
                    if (nameValue != null && nameValue.length() > 0) {
                        hasParam = true;
                        int idxEqual = nameValue.indexOf('=');
                        if (idxEqual != -1) {
                            sb.append(nameValue.substring(0, idxEqual + 1));

                            // 逐字转码
                            String value = nameValue.substring(idxEqual + 1);

                            // System.out.println("value : " + value);
                            if (value != null && value.length() > 0) {
                                int i = 0;
                                int j = 0; // escape 起点偏移量
                                boolean ascii = true;
                                for (; i < value.length(); i++) {
                                    char c = value.charAt(i);
                                    // System.out.println("c : " + (int) c);
                                    if (c >= 0x4E00 && c <= 0x9FFF
                                            || c >= 0xFE30 && c <= 0xFFA0) {
                                        // System.out.println("ZH C ：" + c);
                                        ascii = false;
                                    } else {
                                        // escape
                                        if (!ascii) {
                                            String escapedStr = URLEncoder
                                                    .encode(value.substring(j,
                                                            i), URL_ENCODING);
                                            sb.append(escapedStr);

                                        }

                                        // 对 '/' 进行编码，防止 ResolveBase 出错
                                        if (c == '/') {
                                            // 二次修改
                                            // 百度图片的 URL 参数值里面有 %2F 会连接不上
                                            sb.append(c);
                                        } else {
                                            sb.append(c);
                                        }

                                        ascii = true;
                                        j = i;
                                    }
                                }
                                // 最后一段
                                if (!ascii && j < i) {
                                    String escapedStr = URLEncoder
                                            .encode(value.substring(j, i),
                                                    URL_ENCODING);
                                    sb.append(escapedStr);
                                }
                            }
                        } else {
                            sb.append(nameValue);
                        }
                        sb.append('&');
                    }
                }

                // 删除最后一个 &
                String escapedUrl = sb.toString();
                if (hasParam) {
                    if (escapedUrl.charAt(escapedUrl.length() - 1) == '&') {
                        escapedUrl = escapedUrl.substring(0,
                                escapedUrl.length() - 1);
                    }
                }
                return escapedUrl;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static boolean isJavascript(String url) {
        // javascript:
        return (null != url) && (url.length() > 10)
                && url.substring(0, 11).equalsIgnoreCase("javascript:");
    }

    public static String getJavascriptCommand(String url) {
        int idx1 = url.indexOf(':');
        int idx2 = url.indexOf(';');

        if (idx1 == -1) {
            idx1 = 0;
        } else {
            idx1 = idx1 + 1;
        }

        if (idx2 == -1) {
            idx2 = url.length();
        }

        return url.substring(idx1, idx2);
    }

    public static boolean isAnchorUrl(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }

        return url.startsWith("#");
    }

    /**
     * @return True if the remote url is valid.
     */
    public static boolean isRemoteUrl(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }

        // page:开头是中转特定协议
        return isHttpUrl(url) || isHttpsUrl(url) || isBrokerUrl(url)
                || isSmsUrl(url); // || Utils.isTenpayUrl(url);
    }

    public static boolean isCustomUrl(String url) {
        return isDttpUrl(url) || isSecurityCacheUrl(url);
    }

    /**
     * 删除自定义前缀，如dttp://，security://等 levijiang 2011-06-17
     * 
     * @param srcUrl
     * @return
     */
    public static String deleteCustomPrefix(final String srcUrl) {
        String url = srcUrl;

        if (isDttpUrl(srcUrl)) {
            url = deleteDttpPrefix(srcUrl);
        } else if (isSecurityCacheUrl(srcUrl)) {
            url = deleteSecurityPrefix(srcUrl);
        } else if (isSecurityFileUrl(srcUrl)) {
            url = deleteSecurityFilePrefix(srcUrl);
        } else if (isBrokerUrl(url)) {
            url = deleteBrokerPrefix(url);
        }

        return url;
    }

    /**
     * 将url添加到Vector中，不重复
     * 
     * @param string
     * @param list
     * @param maxNum
     *            ,超过最大限制了，则移除第一个
     * @author leizheng
     */
    public static void addStringToList(String string, Vector<String> list,
            int maxNum) {
        if (TextUtils.isEmpty(string) || list == null) {
            return;
        }

        boolean isNeed = true;
        for (String s : list) {
            if (s.equalsIgnoreCase(string)) {
                isNeed = false;
            }
        }
        if (isNeed) {
            if (maxNum > 0 && list.size() >= maxNum) {
                list.remove(0);
            }
            list.add(string);
        }
    }

    public static boolean isBrokerUrl(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }
        return url.startsWith("page:") || url.startsWith("hotpre:");
    }

    public static String deleteBrokerPrefix(String url) {
        if (url == null || url.length() == 0) {
            return url;
        }

        return url.replaceFirst("hotpre://", "");
    }

    /**
     * @return True iff the remote url is valid.
     */
    public static boolean isLocalUrl(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }
        return isFileUrl(url);
    }
    

    /**
     * 获取本地url的路径（去除file://等前缀）
     * @param url  
     * @return 去除本地url前缀，若不包含本地url前缀则返回原url
     */
    public static String getLocalFilePathWithoutPrex(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        if (url.startsWith(LOCAL_FILE_PREFIX)) {
            return url.replace(LOCAL_FILE_PREFIX, "");
        }
        return url;
    }

    /**
     * URL前缀如果是security://，则说明打开该URL时，页面内容将会使用该URL的安全信息（安全信息缓存在数据库中）
     * 
     * @param url
     * @return
     */
    public static boolean isSecurityCacheUrl(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }
        return url.startsWith("security://");
    }

    public static boolean isSecurityFileUrl(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }
        return url.startsWith("securityFile://");
    }

    public static String deleteSecurityFilePrefix(String url) {
        if (url == null || url.length() == 0) {
            return url;
        }
        return url.replaceFirst("securityFile://", "");
    }

    public static String deleteSecurityPrefix(String url) {
        if (url == null || url.length() == 0) {
            return url;
        }
        return url.replaceFirst("security://", "");
    }

    /**
     * @return True iff the url is an http: url.
     */
    public static boolean isHttpUrl(String url) {
        return (null != url) && (url.length() > 6)
                && url.substring(0, 7).equalsIgnoreCase("http://");
    }

    /**
     * @return True iff the url is an https: url.
     */
    public static boolean isHttpsUrl(String url) {
        return (null != url) && (url.length() > 7)
                && url.substring(0, 8).equalsIgnoreCase("https://");
    }

    /**
     * @return True iff the url is an file: url.
     */
    public static boolean isFileUrl(String url) {
        return (null != url) && (url.length() > 6)
                && url.substring(0, 7).equalsIgnoreCase(LOCAL_FILE_PREFIX);
    }

    /**
     * @return True iff the url is an rtsp: url.
     */
    public static boolean isRtspUrl(String url) {
        return (null != url) && (url.length() > 6)
                && url.substring(0, 7).equalsIgnoreCase("rtsp://");
    }

    /**
     * @return True iff the url is an rtsp: url.
     */
    public static boolean isFtpUrl(String url) {
        return (null != url) && (url.length() > 5)
                && url.substring(0, 6).equalsIgnoreCase("ftp://");
    }

    /**
     * @return True if the url is a sms url.
     */
    public static boolean isSmsUrl(String url) {
        return (null != url) && (url.length() > 4)
                && url.substring(0, 4).equalsIgnoreCase("sms:");
    }

    /**
     * @return True if the url is a tel url.
     */
    public static boolean isTelUrl(String url) {
        return (null != url) && (url.length() > 4)
                && url.substring(0, 4).equalsIgnoreCase("tel:");
    }

    /**
     * @return True if the url is a mail url.
     */
    public static boolean isMailUrl(String url) {
        return (null != url) && (url.length() > 7)
                && url.substring(0, 7).equalsIgnoreCase("mailto:");
    }

    public static boolean isWtaiUrl(String url) {
        return (null != url) && (url.length() > 13)
                && url.substring(0, 13).equalsIgnoreCase("wtai://wp/mc;");
    }

    public static String getWtaiUrl(String url) {
        if (isWtaiUrl(url)) {
            int pos = url.indexOf("?", 13);
            if (pos != -1) {
                return url.substring(13, pos);
            } else {
                return url.substring(13);
            }
        }
        return null;
    }

    /**
     * @return True if the url is a mtt style url.
     */
    public static boolean isMttUrl(String url) {
        return (null != url) && (url.length() > 6)
                && url.substring(0, 6).equalsIgnoreCase("mtt://");
    }

    /**
     * Get command from mtt url.
     */
    public static String getMttCommand(String url) {
        if (url == null) {
            return null;
        }

        int index = url.indexOf("://");

        if (index == -1) {
            index = 0;
        } else {
            index += 3;
        }

        return url.substring(index);
    }

    /**
     * url 是否以指定 prefix 前缀打头
     * 
     * @param url
     * @return
     */
    public static boolean isWapPrefix(String url) {
        if (null == url || url.length() == 0) {
            return false;
        }
        int idx = url.indexOf("://");
        if (idx != -1) {
            url = url.substring(idx + 3);
        }

        // if (url.contains(".3g.qq.com"))
        // return false;

        return url.startsWith("wap.") || url.contains(".3g.")
                || url.startsWith("3g.") || url.contains(".wap.");
        // 去掉了url.contains(".3g.")，为了http://sd31.3g.qq.com/g/s?aid=index_a，不确定是否会引出其他问题
    }

    public static boolean isDttpUrl(String url) {
        if (null == url || url.length() == 0) {
            return false;
        }
        return url.toLowerCase().startsWith("dttp://");
    }

    public static String deleteDttpPrefix(String url) {
        if (url == null) {
            return null;
        }

        return url.toLowerCase().replaceFirst("dttp://", "");
    }

    /**
     * Resolve the base of a URL.
     * 
     * @param base
     * @param rel
     * @return The combined absolute URL
     */
    public static String resolveBase(String base, String rel) {
        if (rel == null) {
            return base;
        }
        // # 开头的anchor地址，不做处理
        if (rel.startsWith("#")) {
            return rel;
        }
        // rel 为有协议头的绝对地址或 JS 指令，不做处理
        if (isRemoteUrl(rel) || isLocalUrl(rel) || isJavascript(rel)) {
            return rel;
        }

        String protocol;
        int i = base.indexOf(':');
        if (i != -1) {
            protocol = base.substring(0, i + 1);
            base = "http:" + base.substring(i + 1);
        } else {
            protocol = null;
        }
        URL url;
        try {
            // Bug fixed:7048264
            // url = new URL(new URL(base), rel);

            // 经过这步的处理, 如果是一个path和file都空的url地址，则已经补了斜杠，则对系统来说是一个可用的url了
            url = toURL(base);

            // 进行特殊字符的替换
            base = url.toString();

            String legalBase = prepareUrl(base);
            String legalRel = prepareUrl(rel);

            // 开始进行resolvebase了
            URI baseURI = new URI(legalBase);
            URI destURI = baseURI.resolve(legalRel);
            url = destURI.toURL();

            // System.out.println("==>> base : " + base);
            // System.out.println("==>> rel : " + rel);
            // System.out.println("==>> dest : " + url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

        if (protocol != null) {
            base = url.toString();
            i = base.indexOf(':');
            if (i != -1) {
                base = base.substring(i + 1);
            }
            base = protocol + base;

            // 如果url中存在../，则清楚掉，标准浏览器做法
            String trueBase = base.replaceAll("\\.\\.\\/", "");
            return trueBase;
        } else {
            return url.toString();
        }
    }

    /**
     * 去掉协议头 Strip the scheme prefix
     * 
     * @param url
     * @return The URL with no scheme prefix
     * @exception MalformedURLException
     */
    public static String stripSchemePrefix(String url) {
        int idx = url.indexOf("://");
        if (idx != -1) {
            return url.substring(idx + 3);
        }
        return url;
    }

    /**
     * Strip the anchor tag from the URL.
     * 
     * @param url
     * @return The URL with no anchor tag.
     * @exception MalformedURLException
     */
    public static String stripAnhcor(String url) throws MalformedURLException {
        int anchorIndex = url.indexOf('#');
        if (anchorIndex != -1) {
            return url.substring(0, anchorIndex);
        }
        return url;
    }

    /**
     * Strip the query string from a URL.
     * 
     * @param url
     *            The URL to examine.
     * @return The URL with no query string.
     * @exception MalformedURLException
     */
    public static URL stripQuery(URL url) throws MalformedURLException {
        String file = url.getFile();
        int i = file.indexOf("?");
        if (i == -1) {
            return url;
        }
        file = file.substring(0, i);
        return new URL(url.getProtocol(), url.getHost(), url.getPort(), file);
    }


    public static String getDefaultExtensionByMimeType(String mimeType) {
        String extension = null;

        if (TextUtils.isEmpty(mimeType)) {
            return null;
        }

        if (mimeType != null && mimeType.toLowerCase().startsWith("text/")) {
            if (mimeType.equalsIgnoreCase("text/html")) {
                extension = ".html";
            } else {
                extension = ".txt";
            }
        } else if (mimeType != null
                && mimeType.toLowerCase().startsWith("image/")) {
            if (mimeType.equalsIgnoreCase("image/png")) {
                extension = ".png";
            } else if (mimeType.equalsIgnoreCase("image/jpeg")) {
                extension = ".jpeg";
            } else if (mimeType.equalsIgnoreCase("image/jpg")) {
                extension = ".jpg";
            } else if (mimeType.equalsIgnoreCase("image/gif")) {
                extension = ".gif";
            }
        } else {
            extension = ".bin";
        }

        return extension;
    }

    public static String getMimeTypeByFilePath(final String filePath) {
        String mimeType = null;

        if (TextUtils.isEmpty(filePath)) {
            return null;
        }

        if (filePath.endsWith(".png")) {
            mimeType = "image/png";
        } else if (filePath.endsWith(".jpg")) {
            mimeType = "image/jpg";
        } else if (filePath.endsWith(".jpeg")) {
            mimeType = "image/jpeg";
        } else if (filePath.endsWith(".gif")) {
            mimeType = "image/gif";
        } else if (filePath.endsWith(".js")) {
            mimeType = "application/x-javascript";
        } else if (filePath.endsWith(".css")) {
            mimeType = "text/css";
        } else if (filePath.endsWith(".ttf")) {
            mimeType = "application/x-font-ttf";
        } else if (filePath.endsWith(".html")) {
            mimeType = "text/html";
        }

        return mimeType;
    }

    /**
     * 获取 url host 比如 http://localhost/images/ab.jpg 提取 host 段： localhost
     * 
     * @param url
     * @return
     */
    public static String getHost(String url) {
        if (url == null || url.length() == 0) {
            return null;
        }
        int start = 0;
        int protoIdx = url.indexOf("://");
        if (protoIdx != -1) {
            start = protoIdx + 3;
        }
        String host = null;
        int slashIdx = url.indexOf('/', start);
        if (slashIdx != -1) {
            host = url.substring(start, slashIdx);
        } else {
            host = url.substring(start);
        }

        // 去除端口号
        int colonPos = host.indexOf(":");
        if (colonPos >= 0) {
            host = host.substring(0, colonPos);
        }

        return host;
    }

    public static String getFilePath(String url) {
        if (url == null || url.length() == 0) {
            return null;
        }
        int start = 0;
        int protoIdx = url.indexOf("://");
        if (protoIdx != -1) {
            start = protoIdx + 3;
        }
        int slashIdx = url.indexOf('/', start);
        int paramIdx = url.indexOf('?');
        if (slashIdx != -1) {
            int endIdx = paramIdx != -1 ? paramIdx : url.length();
            String relativePath = url.substring(slashIdx, endIdx);
            return relativePath;
        }

        return null;
    }

    /**
     * 获取顶级域名 比如 http://3g.qq.com/images/ab.jpg 提取 root 段：qq.com
     * 
     * @param url
     * @return
     */
    public static String getRootDomain(String url) {
        String rootDomain = null;
        String host = getHost(url);
        // System.out.println("host : " + host);
        if (host != null && !"".equals(host)) {
            char dot = '.';
            String domainSuffix = null;
            int lastIdx = host.lastIndexOf(dot);
            if (lastIdx != -1) {
                domainSuffix = host.substring(lastIdx + 1);
                String hostWithoutSuffix = host.substring(0, lastIdx);

                // 兼容两级域名后缀，如 .com.cn
                if (domainSuffix != null && domainSuffix.equalsIgnoreCase("cn")) {
                    lastIdx = hostWithoutSuffix.lastIndexOf(dot);
                    if (lastIdx != -1) {
                        String mainDomainSuffix = hostWithoutSuffix
                                .substring(lastIdx + 1);
                        if (mainDomainSuffix != null
                                && mainDomainSuffix.length() > 0) {
                            if (mainDomainSuffix.equalsIgnoreCase("com")
                                    || mainDomainSuffix.equalsIgnoreCase("edu")
                                    || mainDomainSuffix.equalsIgnoreCase("gov")) {
                                domainSuffix = mainDomainSuffix.concat(
                                        String.valueOf(dot)).concat(
                                        domainSuffix);
                                hostWithoutSuffix = hostWithoutSuffix
                                        .substring(0, lastIdx);
                            }
                        }
                    }
                }

                String domainName = null;
                int domainIdx = hostWithoutSuffix.lastIndexOf(dot);
                if (domainIdx != -1) {
                    domainName = hostWithoutSuffix.substring(domainIdx + 1);
                } else {
                    domainName = hostWithoutSuffix;
                }
                if (domainName != null && domainName.length() > 0) {
                    rootDomain = domainName.concat(String.valueOf(dot)).concat(
                            domainSuffix);
                }
            }
        }
        return rootDomain;
    }

    public static boolean isQQDomain(String url) {
        String domain = getRootDomain(url);
        if (domain != null && !TextUtils.isEmpty(domain)) {
            return domain.equals("qq.com");
        }
        return false;
    }

    /**
     * 获取 url path 比如 http://localhost/images/ab.jpg 提取 path 段： images/ab.jpg
     * 
     * @param url
     * @return
     */
    public static String getPath(String url) {
        if (url == null || url.length() == 0) {
            return null;
        }
        int start = 0;
        int protoIdx = url.indexOf("://");
        if (protoIdx != -1) {
            start = protoIdx + 3;
        }
        String path = null;
        int slashIdx = url.indexOf('/', start);
        if (slashIdx != -1) {
            int questionIdx = url.indexOf('?', slashIdx);
            if (questionIdx != -1) {
                path = url.substring(slashIdx + 1, questionIdx);
            } else {
                path = url.substring(slashIdx + 1);
            }
        }
        return path;
    }

    /** Regex used to parse content-disposition headers */
    private static final Pattern CONTENT_DISPOSITION_PATTERN = Pattern.compile(
            "attachment;\\s*filename\\s*=\\s*(\"?)([^\"]*)\\1\\s*[;\\s*charset=\\s*]*([^\"]*)\\s*$",
            Pattern.CASE_INSENSITIVE);

    /**
     * 将url中的中文按照utl8编码方式解码，解码失败则返回原url。
     * 
     * @param str
     * @return
     */
    public static String decode(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }

        String result = str;
        try {
            result = URLDecoder.decode(str, URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 
     * 大多数JDBC
     * Driver采用本地编码格式来传输中文字符，例如中文字符“0x4175”会被转成“0x41”和“0x75”进行传输。因此需要对JDBC
     * Driver返回的字符以及要发给JDBC Driver的字符进行转换
     * 判断string是否是native，以便将native转换成unicode
     * 
     * @param content
     * @return
     */
    private static boolean isNativeString(String content) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        
        char[] charData = content.toCharArray();
        int length = charData.length;
        byte b = -1;
        for (int i = 0; i < length; i++) {
            b = (byte) (charData[i] >> 8 & 0xff);
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断URL是否是一个有效的格式
     */
//    public static boolean isCandidateUrl(final String aUrl) {
//        if (aUrl == null || aUrl.length() == 0) {
//            return false;
//        }
//
//        if (QubeStringUtil.hasNotAscII(aUrl)) { // 含有中文
//            return false;
//        }
//
//        String url = aUrl.trim();
//
//        Matcher validUrl = mVALIDURL.matcher(url);
//        Matcher validLocal = mVALIDLOCALURL.matcher(url);
//        Matcher validIp = mVALIDIPADDRESS.matcher(url);
//        Matcher validMtt = mVALIDMTTURL.matcher(url);
//        Matcher validQbe = mVALIDQBEURL.matcher(url);
//
//        return (validUrl.find() || validLocal.find() || validIp.find()
//                || validMtt.find() || validQbe.find() || isLocalUrl(url));
//    }

    /**
     * 判断URL是否是一个有效IP的格式
     */
    public static boolean isIpUrl(final String aUrl) {
        if (aUrl == null || aUrl.length() == 0) {
            return false;
        }
        String url = aUrl.trim();

        Matcher validIp = mVALIDIPADDRESS.matcher(url);
        return validIp.find();
    }

    /**
     * 判断当前url是否匹配了白名单域名，只适用于根域名匹配
     * 
     * @return
     */
    public static boolean isUrlInWhiteList(String url, Vector<String> whiteList) {
        if (TextUtils.isEmpty(url) || whiteList == null
                || whiteList.size() == 0) {
            DtLog.d("alipay", "mAliPaySafeList:" + whiteList);
            return false;
        }
        String domain = getRootDomain(url);

        if (domain != null && !TextUtils.isEmpty(domain)) {
            for (int i = 0; i < whiteList.size(); i++) {
                if (whiteList.get(i).contains(domain)) {
                    DtLog.d("alipay", "current URL is in whitelist: url="
                            + url);
                    return true;
                }
            }
        }
        DtLog.d("alipay", "current URL is not in whitelist: url=" + url);
        return false;
    }

    /**
     * 判断是否是内网IP
     */
    public static boolean isInnerIP(String ipAddress) {
        if (TextUtils.isEmpty(ipAddress) || !isIpUrl(ipAddress)) {
            return false;
        }
        // 私有IP： A类 10.0.0.0 - 10.255.255.255
        // B类 172.16.0.0 - 172.31.255.255
        // C类 192.168.0.0 - 192.168.255.255
        // 当然，还有127这个网段是环回地址

        boolean isInnerIp = false;

        try {
            long ipNum = getIpNum(ipAddress);
            long aBegin = getIpNum("10.0.0.0");
            long aEnd = getIpNum("10.255.255.255");
            long bBegin = getIpNum("172.16.0.0");
            long bEnd = getIpNum("172.31.255.255");
            long cBegin = getIpNum("192.168.0.0");
            long cEnd = getIpNum("192.168.255.255");

            isInnerIp = isInner(ipNum, aBegin, aEnd)
                    || isInner(ipNum, bBegin, bEnd)
                    || isInner(ipNum, cBegin, cEnd)
                    || ipAddress.equals("127.0.0.1")
                    || ipAddress.equals("1.1.1.1");
        } catch (Exception e) {
            // 解析出错时，判定为非内网地址
            e.printStackTrace();
        }

        return isInnerIp;

    }

    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }

    /**
     * 获取IP数
     */
    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);

        long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
        return ipNum;
    }

    /**
     * 判断URL是否有一个有效的协议头
     */
    public static boolean hasValidProtocal(final String aUrl) {
        if (aUrl == null || aUrl.length() == 0) {
            return false;
        }
        String url = aUrl.trim().toLowerCase();

        int pos1 = url.indexOf("://");
        int pos2 = url.indexOf('.');

        // 检测"wap.fchgame.com/2/read.jsp?url=http://www.zaobao.com/zg/zg.shtml"类型网址
        if (pos1 > 0 && pos2 > 0 && pos1 > pos2) {
            return false;
        }

        return url.contains("://");
    }

    /**
     * 去除http://头，和末尾的"/"
     * 
     * @param aUrl
     * @return
     */
    public static String resolvValidRecentUrl(final String aUrl) {
        if (aUrl == null || aUrl.length() == 0) {
            return null;
        }

        String url = aUrl;

        return url;
    }

    /**
     * 根据输入，得到一个有效URL 如果输入无法被解析为一个URL，返回将输入作为搜索KEY的搜索URL,soso?key=URL
     * 输入为空，返回NULL
     */
    /*
     * +++ public static String getUrlAsSearchKeyOrAddress(final String aUrl) {
     * if (aUrl == null || aUrl.length() == 0) { return null; } String recUrl =
     * resolvValidUrl(aUrl); if (recUrl == null || recUrl.startsWith("#")) { try
     * { String searchEngine = QubeAppEngine.getInstance()
     * .getSearchManager().getPageSearchEngineUrl();
     * 
     * recUrl = URLEncoder.encode(aUrl, "UTF-8");
     * 
     * return (searchEngine + recUrl); } catch (Exception e) { // Log.d(TAG,
     * "can't encode URL,error:" + // e.getLocalizedMessage()); return null; }
     * 
     * } else { return recUrl; } }
     */

    /**
     * Get sms uri from url
     */
    public static Uri getSmsUriFromUrl(String url) {
        if (isSmsUrl(url)) {
            int index = url.indexOf('?');
            if (index > -1) {
                return Uri.parse(url.substring(0, index));
            } else {
                return Uri.parse(url);
            }
        }

        return null;
    }

    /**
     * Get sms text from url.
     */
    public static String getSmsTextFromUrl(String url) {
        if (isSmsUrl(url)) {
            int index = url.indexOf('?');
            if (index > -1 && index < (url.length() - 1)) {
                String[] params = url.substring(index + 1).split("&");
                for (String param : params) {
                    if (param.startsWith("body=")) {
                        int i = param.indexOf('=');
                        if (i > -1 && i < (param.length() - 1)) {
                            return param.substring(i + 1);
                        }
                    }
                }
            } else {
                return null;
            }
        }

        return null;
    }

    /**
     * 字符串是否是合法的url字符
     * 
     * @param str
     * @return true:合法的url字符，false:含有非法的url字母
     */
    public static boolean isValidUrlLetter(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        return mVALIDURLLETTER.matcher(str).find();
    }
    
    /**
     * 判断是否有可用网络
     * 
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context == null) {
            return false;
        }

        try {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                
                NetworkInfo info = manager.getActiveNetworkInfo();                
                if (info != null && info.isConnected()) {
                    return true;
                }
            }            
            return false;
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        // 异常情况下默认网络ok
        return true;
    }

    /**
     * 判断当前连接是否是qbe连接
     * 
     * @param url
     * @return
     */
    public static boolean isQubeUrl(String url) {
        if (url == null) {
            return false;
        }

        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return url.startsWith("qube://");
    }

    // 这段内容用于猜测字符串的编码格式
    public static final byte CHARACTER_ENCODING_ASCII = 0;
    public static final byte CHARACTER_ENCODING_UTF8 = 1;
    public static final byte CHARACTER_ENCODING_GB18030 = 2;

    public static byte guessCharacterEncoding(byte[] data) {
        byte asciiCount = 0;
        int i = 0;

        for (i = 0; i < data.length;) {
            byte c = data[i];

            if ((c & 0x80) == 0) {
                asciiCount++;
                i++;
            } else if ((c & 0xE0) == 0xC0) {
                if (!checkUtf8Char(data, i + 1, 1)) {
                    return CHARACTER_ENCODING_GB18030;
                }

                i += 2;
            } else if ((c & 0xF0) == 0xE0) {
                if (!checkUtf8Char(data, i + 1, 2)) {
                    return CHARACTER_ENCODING_GB18030;
                }
                i += 3;
            } else if ((c & 0xF8) == 0xF0) {
                if (!checkUtf8Char(data, i + 1, 3)) {
                    return CHARACTER_ENCODING_GB18030;
                }
                i += 4;
            } else if ((c & 0xFC) == 0xF8) {
                if (!checkUtf8Char(data, i + 1, 4)) {
                    return CHARACTER_ENCODING_GB18030;
                }
                i += 5;
            } else if ((c & 0xC0) == 0x80) {
                // 证明这个是UTF-8的第二个字节或者第三个字节
                i++;
            } else {
                return CHARACTER_ENCODING_GB18030;
            }
        }

        if (asciiCount == data.length) {
            return CHARACTER_ENCODING_ASCII;
        } else {
            return CHARACTER_ENCODING_UTF8;
        }
    }

    private static boolean checkUtf8Char(byte[] data, int offset, int len) {
        for (int i = 0; i < len && offset + i < data.length; i++) {
            if ((data[offset + i] & 0xC0) != 0x80) {
                return false;
            }
        }

        return true;
    }
    
    /**
     * 判读是否是能预览的图片格式
     * 
     * @param url
     * @return
     */
    public static boolean isImage(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        url = url.toLowerCase();
        if (url.endsWith(".png") || url.endsWith(".jpg")
                || url.endsWith(".jpeg") || url.endsWith(".bmp")
                || url.endsWith(".gif") || url.endsWith(".webp")) {
            return true;
        }
        return false;
    }
    
    /**
     * 判读是否是视频格式
     * 
     * @param url
     * @return
     */
    public static boolean isVideo(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        url = url.toLowerCase();
        if (url.endsWith(".mp4") || url.endsWith(".3gp")
                || url.endsWith(".m3u8") || url.startsWith("rtsp://")) {
            return true;
        }
        return false;
    }
    
	/**
	 * 解析当前url的host和path信息
	 *    -- host：为"://"到第一个'/'之间的信息，path为'/'之后的部分
	 * @param strTargetUrl
	 * @return String[2]; 0 -- host信息， 1-- path信息
	 */
	public static String[] parseUrlForHostPath(String strTargetUrl) {
		
		String host = "";
		String path = "";
		if (!TextUtils.isEmpty(strTargetUrl)) {
			int hostIndex = strTargetUrl.indexOf("://") + 3;
			if (hostIndex < 0) {
				hostIndex = 0;
			}
			int pathIndex = strTargetUrl.indexOf('/', hostIndex);
			if (pathIndex < 0) {
				host = strTargetUrl.substring(hostIndex);
				path = "";
			} else {
				host = strTargetUrl.substring(hostIndex, pathIndex);
				path = strTargetUrl.substring(pathIndex);			
			}
		}
		
		if (TextUtils.isEmpty(path)) {
			path = "/";
		}		
		return new String[] {host, path };
	}
    
}
