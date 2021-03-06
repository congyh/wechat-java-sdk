package com.github.congyh.util.json;

import com.github.congyh.api.WeChatConst;
import com.github.congyh.model.WeChatAccessToken;
import com.github.congyh.model.menu.*;
import com.github.congyh.util.HttpsUtils;
import com.google.gson.Gson;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 测试通过Https get获取access_token, 并转化为WeChatAccessToken对象
 *
 * @author <a href="mailto:yihao.cong@outlook.com">Cong Yihao</a>
 */
@Deprecated
public class HttpsTest {
    private static final Logger logger = Logger.getLogger(HttpsTest.class.getName());

    public static void main(String[] args)
        throws IOException, NoSuchProviderException,
        NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String tokenUrl = WeChatConst.ACCESS_TOKEN_URL_PREFIX
            + "grant_type=client_credential&appid="
            + WeChatConst.APPID
            + "&secret="
            + WeChatConst.SECRET;
        logger.log(Level.INFO, tokenUrl);
//        URL url = new URL(tokenUrl);
//        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//        TrustManager[] tm  = new TrustManager[] {new MyX509TrustManager()};
//        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
//        sslContext.init(null, tm, new SecureRandom());
//        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//        conn.setSSLSocketFactory(sslSocketFactory);
//        conn.setDoInput(true);
//        // 设置请求方式
//        conn.setRequestMethod("GET");
//        InputStream in = conn.getInputStream();
//        InputStreamReader isr = new InputStreamReader(in, "utf-8");
//        BufferedReader br = new BufferedReader(isr);
//        StringBuffer buffer = new StringBuffer();
//        String str = null;
//        while ((str = br.readLine()) != null) {
//            buffer.append(str);
//        }
//        br.close();
//        isr.close();
//        in.close();
//        conn.disconnect();
//        logger.log(Level.INFO, GsonBuilderInitializer
//            .builder.create().fromJson(buffer.toString(), WeChatAccessToken.class).toString());

        WeChatAccessToken accessToken = new Gson()
            .fromJson(HttpsUtils.get(tokenUrl), WeChatAccessToken.class);
        String postURL = WeChatConst.MENU_CREATE_URL_PREFIX + accessToken.getAccessToken();
        logger.info(postURL);

        ClickButton cbtn1 = new ClickButton();
        cbtn1.setName("今日问候");
        cbtn1.setType("click");
        cbtn1.setKey("V1001_GREETINGS");

        ViewButton vbtn1 = new ViewButton();
        vbtn1.setName("团队主页");
        vbtn1.setType("view");
        vbtn1.setUrl("http://congyh.com");

        ClickButton bws1 = new ClickButton();
        bws1.setName("加入我们");
        bws1.setType("click");
        bws1.setKey("V1001_JOIN_US");

        ClickButton bws2 = new ClickButton();
        bws2.setName("给我们点赞");
        bws2.setType("click");
        bws2.setKey("V1001_THUMB_UP");

        ButtonWithSubbuttons bws = new ButtonWithSubbuttons();
        bws.setName("更多");
        bws.setButtons(new Button[] {bws1, bws2});

        Menu menu = new Menu();
        menu.setButtons(new Button[] {cbtn1, vbtn1, bws});

        String jsonMenu = new Gson().toJson(menu);
        logger.info(jsonMenu);

        // 注意: 这里如果选择ContentType是text plain的话, 中文会出现乱码, JSON字符串需要设置为APPLICATION_JSON
        String returnJson = HttpsUtils.post(postURL, new StringEntity(jsonMenu, ContentType.APPLICATION_JSON));
        logger.info(returnJson);
    }
}
