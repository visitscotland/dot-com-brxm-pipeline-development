package com.visitscotland.brmx.utils;

import com.visitscotland.brmx.beans.Image;
import com.visitscotland.brmx.beans.mapping.FlatImage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CommonUtils {

    //TODO use utils library instead.
    public static final boolean isEmpty(String value){
        return value == null || value.trim().length() == 0;
    }

    public static final String contentIssue (String message, Object... parameters){
        return String.format("- [CONTENT] - " + message, parameters);
    }

    /**
     * TODO comment!
     * @param productId
     * @param locale
     * @return
     * @throws IOException
     */
    public static JSONObject getProduct(String productId, Locale locale) throws IOException {
        if (!CommonUtils.isEmpty(productId)) {
            String dmsUrl = Properties.VS_DMS_SERVICE + "/data/products/card?id=" + productId;
            if (locale != null) {
                dmsUrl += "&locale=" + locale.getLanguage();
            }

            String responseString = request(dmsUrl);
            if (responseString!=null) {
                JSONObject json = new JSONObject(responseString);

                if (json.has("data")) {
                    return json.getJSONObject("data");
                }
            }
        }
        return null;
    }

    /**
     * Request a page and return the body as String
     * @param url
     * @return null if status code not 200 or 300
     * @throws IOException
     */
    public static String request(String url) throws IOException {
        // TODO comment
        if (((HttpURLConnection) new URL(url).openConnection()).getResponseCode() < 400){
            final BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            final StringBuilder sb = new StringBuilder();
            int cp;

            while ((cp = br.read()) != -1) {
                sb.append((char) cp);
            }

            return sb.toString();
        }
        return null;
    }

    public static FlatImage getTranslatedImage(Image cmsImage, Locale locale){

        FlatImage flatImage = new FlatImage(cmsImage);
        flatImage.setCredit(cmsImage.getCredit());
        if (locale == null){
            flatImage.setAltText(cmsImage.getAltText());
            flatImage.setDescription(cmsImage.getDescription());
        }else{
            switch (locale.getLanguage()) {
                case "fr":
                    if (cmsImage.getFr()!= null) {
                        flatImage.setAltText(cmsImage.getFr().getAltText());
                        flatImage.setDescription(cmsImage.getFr().getCaption());
                    }
                    break;
                case "de":
                    if (cmsImage.getDe()!= null) {
                        flatImage.setAltText(cmsImage.getDe().getAltText());
                        flatImage.setDescription(cmsImage.getDe().getCaption());
                    }
                    break;
                case "es":
                    if (cmsImage.getEs()!= null) {
                        flatImage.setAltText(cmsImage.getEs().getAltText());
                        flatImage.setDescription(cmsImage.getEs().getCaption());
                    }
                    break;
                case "nl":
                    if (cmsImage.getNl()!= null) {
                        flatImage.setAltText(cmsImage.getNl().getAltText());
                        flatImage.setDescription(cmsImage.getNl().getCaption());
                    }
                    break;
                 case "it":
                    if (cmsImage.getIt()!= null) {
                        flatImage.setAltText(cmsImage.getIt().getAltText());
                        flatImage.setDescription(cmsImage.getIt().getCaption());
                    }
                    break;
                default:
                    flatImage.setAltText(cmsImage.getAltText());
                    flatImage.setDescription(cmsImage.getDescription());
            }
            if (flatImage.getAltText()==null){
                flatImage.setAltText(cmsImage.getAltText());
            }
           if (flatImage.getDescription()==null){
                flatImage.setDescription(cmsImage.getDescription());
            }


        }
        return flatImage;
    }


    //TODO this method returns the current open state and it coud be affected by the cache, ask WEBOPS and move it to front end if needed
    public static  String currentOpenStatus(String starTime, String endTime, Locale locale){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma");
        LocalTime starts = LocalTime.parse(starTime, formatter);
        LocalTime ends = LocalTime.parse(endTime, formatter);
        LocalTime currentTime = LocalTime.now(ZoneId.of("+1"));
        if (currentTime.isAfter(starts) && currentTime.isBefore(ends)){
            if (currentTime.plusMinutes(30).isAfter(ends)){
                return  HippoUtils.getResourceBundle("stop.close.soon", "itinerary", locale);
            }else{
                return   HippoUtils.getResourceBundle("stop.open", "itinerary", locale);
            }
        }else
        {
            return   HippoUtils.getResourceBundle("stop.closed", "itinerary", locale);
        }
    }
}