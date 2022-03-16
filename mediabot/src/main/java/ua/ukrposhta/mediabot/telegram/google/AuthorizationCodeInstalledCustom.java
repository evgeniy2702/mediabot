package ua.ukrposhta.mediabot.telegram.google;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthorizationCodeInstalledCustom {
    private final AuthorizationCodeFlow flow;
    private final VerificationCodeReceiver receiver;
    private static final Logger LOGGER = Logger.getLogger(AuthorizationCodeInstalledCustom.class.getName());
    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);
    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";
    @Autowired
    private Context context;

    public AuthorizationCodeInstalledCustom(AuthorizationCodeFlow flow, VerificationCodeReceiver receiver) {
        this.flow = Preconditions.checkNotNull(flow);
        this.receiver = Preconditions.checkNotNull(receiver);
    }

    public Credential authorize(String userId) throws IOException {

        telegramLogger.info("start authorize method in AuthorizationCodeInstalledCustom.class");
        consoleLogger.info("start authorize method in AuthorizationCodeInstalledCustom.class");

        Credential var3;
        try {
            Credential credential = this.flow.loadCredential(userId);
            if (credential == null || credential.getRefreshToken() == null && credential.getExpiresInSeconds() != null && credential.getExpiresInSeconds() <= 60L) {
                String redirectUri = this.receiver.getRedirectUri();
                AuthorizationCodeRequestUrl authorizationUrl = this.flow.newAuthorizationUrl().setRedirectUri(redirectUri);
                this.onAuthorization(authorizationUrl);
                String code = this.receiver.waitForCode();
                TokenResponse response = this.flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
                Credential var7 = this.flow.createAndStoreCredential(response, userId);
                return var7;
            }

            var3 = credential;
        }
        catch (Exception e){
            var3 = null;
            e.getStackTrace();
            LOGGER.info("User rejected Google API Sheets authorization");
            telegramLogger.error("User rejected Google API Sheets authorization");
            consoleLogger.error("User rejected Google API Sheets authorization");
        }
        finally {
            this.receiver.stop();
        }

        return var3;
    }

    protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) {
        browse(authorizationUrl.build());
    }

    public  void browse(String url) {

        String javaVendor = System.getProperty("java.vendor");
        System.out.println("javaVendor " + javaVendor);
        String javaSpecificationVendor = System.getProperty("java.specification.vendor");
        System.out.println("javaSpecificationVendor " + javaSpecificationVendor);
        String javaVendorUrl = System.getProperty("java.vendor.url");
        System.out.println("javaVendorUrl " + javaVendorUrl);
        String javaVmSpecificationVendor = System.getProperty("java.vm.specification.vendor");
        System.out.println("javaVmSpecificationVendor " + javaVmSpecificationVendor);
        String javaVmSpecificationName = System.getProperty("java.vm.specification.name");
        System.out.println("javaVmSpecificationName " + javaVmSpecificationName);
        String osName = System.getProperty("os.name");
        System.out.println("os.name " + osName);
        String getSecurityManager  = System.getSecurityManager().toString();
        System.out.println("getSecurityManager  " + getSecurityManager);
        String javaRuntimeName  = System.getProperty("java.runtime.name");
        System.out.println("javaRuntimeName  " + javaRuntimeName);
        String androidVmDexfile  = System.getProperty("android.vm.dexfile");
        System.out.println("androidVmDexfile  " + androidVmDexfile);

        boolean androidOrOs = "The Android Project".equals(System.getProperty("java.vendor"));

        if(!androidOrOs) {
            String os = System.getProperty("os.name").toLowerCase(); // получаем имя операционной системы
            Runtime rt = Runtime.getRuntime();

            try {
                if (os.contains("win")) {
                    // не поддерживаются ссылки формата "leodev.html#someTag"
                    rt.exec("rundll32 url.dll,FileProtocolHandler " + url); // если windows, открываем урлу через командную строку
                } else if (os.contains("mac")) {
                    rt.exec("open " + url); // аналогично в MAC
                } else if (os.contains("nix") || os.contains("nux")) {
                    // c nix системами все несколько проблемотичнее
                    String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera", "links", "lynx"};
                    // Формируем строку с вызовом всем браузеров через логическое ИЛИ в shell консоли
                    // "browser0 "URI" || browser1 "URI" ||..."
                    StringBuilder cmd = new StringBuilder();
                    for (int i = 0; i < browsers.length; i++)
                        cmd.append(i == 0 ? "" : " || ").append(browsers[i]).append(" \"").append(url).append("\" ");
                    rt.exec(new String[]{"sh", "-c", cmd.toString()});
                }
            } catch (Exception e) {
                e.getStackTrace();
                LOGGER.log(Level.WARNING, "Unable to open browser " + os, e);
                telegramLogger.error("Unable to open browser " + os + e);
                consoleLogger.error("Unable to open browser " + os + e);
            }
        } else {

            if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
                url = HTTP + url;
            }
            try {

                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
                context.startActivity(Intent.createChooser(intent, "Choose browser"));

            } catch (Exception e) {
                e.getStackTrace();
                LOGGER.log(Level.WARNING, "Unable to open browser on Android ", e);
                telegramLogger.error("Unable to open browser on Android " + e);
                consoleLogger.error("Unable to open browser on Android " + e);
            }
        }
    }

    public final AuthorizationCodeFlow getFlow() {
        return this.flow;
    }

    public final VerificationCodeReceiver getReceiver() {
        return this.receiver;
    }
}
