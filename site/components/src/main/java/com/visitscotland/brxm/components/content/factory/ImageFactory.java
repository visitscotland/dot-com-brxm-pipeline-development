package com.visitscotland.brxm.components.content.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.beans.ExternalLink;
import com.visitscotland.brxm.beans.Image;
import com.visitscotland.brxm.beans.ImageData;
import com.visitscotland.brxm.beans.InstagramImage;
import com.visitscotland.brxm.beans.dms.LocationObject;
import com.visitscotland.brxm.beans.mapping.Coordinates;
import com.visitscotland.brxm.beans.mapping.FlatImage;
import com.visitscotland.brxm.beans.mapping.Module;
import com.visitscotland.brxm.dms.LocationLoader;
import com.visitscotland.brxm.utils.CommonUtils;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
public class ImageFactory {

    private static final Logger logger = LoggerFactory.getLogger(ImageFactory.class);

    LocationLoader locationLoader;

    public ImageFactory(){
        locationLoader = LocationLoader.getInstance();
    }

    public FlatImage getImage(HippoBean image, Module module, Locale locale){
        if (image instanceof InstagramImage) {
            return createImage((InstagramImage) image, module, locale);
        } else if (image  instanceof Image) {
            return createImage((Image) image, module, locale);
        } else if (image instanceof ExternalLink){
            FlatImage flat = new FlatImage();
            // TODO: No alt text & no description: Double check the requirements
            flat.setExternalImage(((ExternalLink) image).getLink());
        }
        return null;
    }

    /**
     * Creates an Image from an Instagram compound (InstagramLink)
     *
     * @param cmsImage Compound with the information for the image
     * @param module Module where potential issues will be logged
     * @param locale Locale for translated texts (i.e. location)
     *
     * @return FlatImage Object
     */
    public FlatImage createImage(Image cmsImage, Module module, Locale locale){
        FlatImage image = new FlatImage();
        ImageData data = null;

        image.setCmsImage(cmsImage);
        //Populate altText and Caption
        if (locale != null && locale.getLanguage() != null) {
            //TODO: Use the new Language class when it is available
            if ("fr".equals(locale.getLanguage())) {
                data = cmsImage.getFr();
            } else if ("de".equals(locale.getLanguage())) {
                data = cmsImage.getDe();
            } else if ("es".equals(locale.getLanguage())) {
                data = cmsImage.getEs();
            } else if ("nl".equals(locale.getLanguage())) {
                data = cmsImage.getNl();
            } else if ("it".equals(locale.getLanguage())) {
                data = cmsImage.getIt();
            }
        }
        if (data == null) {
            image.setAltText(cmsImage.getAltText());
            image.setDescription(cmsImage.getDescription());
        } else {
            image.setAltText(data.getAltText());
            image.setDescription(data.getCaption());
        }

        if (Contract.isEmpty(image.getAltText())){
            module.addErrorMessage("The image does not have an Alternative Text for the language " + locale);
            //TODO Do we need content log for this?
            //TODO Is info level enough for this issue?
            logger.info("The image does not have an Alternative Text for the language {}", locale);
        }

        if (Contract.isEmpty(image.getDescription())){
            module.addErrorMessage("The image does not have a description for the locale " + locale);
            //TODO Do we need content log for this?
            //TODO Is info level enough for this issue?
            logger.info("The image does not have a description for the locale {}", locale);
        }

        //Populates the location
        populateLocation(image, cmsImage.getLocation(), locale);

        return image;
    }

    /**
     * Creates an Image from an Instagram compound (InstagramLink)
     *
     * @param document Compound with the information for the image
     * @param module Module where potential issues will be logged
     * @param locale Locale for translated texts (i.e. location)
     *
     * @return FlatImage Object
     */
    public FlatImage createImage(InstagramImage document, Module module, Locale locale){
        try {
            JsonNode instagramInfo = CommonUtils.getInstagramInformation(document);

            if (instagramInfo != null) {
                FlatImage image = new FlatImage();
                image.setExternalImage(instagramInfo.get("thumbnail_url").asText());
                image.setCredit(instagramInfo.get("author_name").asText());
                image.setAltText(document.getCaption());
                image.setDescription(document.getCaption());
                image.setSource(FlatImage.Source.INSTAGRAM);
                image.setPostUrl(Properties.INSTAGRAM_API  + document.getId());

                populateLocation(image, document.getLocation(), locale);

                return image;
            } else {
                module.addErrorMessage("The Instagram id is no longer valid");
                //TODO Code smell
                logger.warn(CommonUtils.contentIssue("The Instagram id %s is no longer, Listicle = %s ",
                        document.getId(), document.getPath()));
            }
        } catch (IOException e) {
            module.addErrorMessage("Error while accessing Instagram: " + e.getMessage());
            logger.error("Error while accessing Instagram", e);
        }

        return null;
    }

    /**
     * Populates the fields Location and Coordinates from the DMS information providing translated locations for
     * all images.
     *
     * TODO Use for instagram;
     */
    private void populateLocation(FlatImage image, String location, Locale locale){
        LocationObject locationObject = locationLoader.getLocation(location, locale);
        if (locationObject!=null) {
            if (image.getCoordinates() == null) {
                image.setCoordinates(new Coordinates(locationObject.getLatitude(), locationObject.getLongitude()));
            }
            //TODO Probar
            image.setLocation(locationObject.getName());
        }
    }
}
