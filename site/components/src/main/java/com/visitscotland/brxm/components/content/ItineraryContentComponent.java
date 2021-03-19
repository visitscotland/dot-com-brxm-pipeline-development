package com.visitscotland.brxm.components.content;


import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.model.*;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.model.Coordinates;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.services.CommonUtilsService;
import com.visitscotland.brxm.dms.LocationLoader;
import com.visitscotland.utils.CoordinateUtils;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;


public class ItineraryContentComponent extends PageContentComponent<Itinerary> {

    private static final Logger logger = LoggerFactory.getLogger(ItineraryContentComponent.class);

    public final String STOPS_MAP = "stops";

    public final String DISTANCE = "distance";
    public final String FIRST_STOP_LOCATION = "firstStopLocation";
    public final String LAST_STOP_LOCATION = "lastStopLocation";

    private ResourceBundleService bundle;
    private LocationLoader locationLoader;
    private DMSDataService dmsData;

    public ItineraryContentComponent() {
        logger.debug("ItineraryContentComponent initialized");

        bundle = VsComponentManager.get(ResourceBundleService.class);
        locationLoader = VsComponentManager.get(LocationLoader.class);
        dmsData = VsComponentManager.get(DMSDataService.class);
    }

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        generateStops(request);
        addHeroCoordinates(request);
    }


    /**
     * @param request HstRequest
     */
    private void generateStops(HstRequest request) {

        final Itinerary itinerary = getDocument(request);
        final String OTYML = "otyml";
        final String ITINERARY_ALERTS = "alerts";
        final String ADDRESS = "address";
        final String LOCATION = "city";
        final String URL = "url";
        final String TIME_TO_EXPLORE = "timeToExplore";
        final String PRICE = "price";
        final String DISPLAY_PRICE = "displayPrice";
        final String NAME = "name";
        final String LAT = "latitude";
        final String LON = "longitude";
        final String OPENING = "todayOpeningTime";
        final String START_TIME = "startTime";
        final String END_TIME = "endTime";
        final String IMAGE = "images";
        final String OPENING_DAY = "day";
        final String OPENING_STATE = "state";
        //TODO change provivisional to provisional on the DMS product project
        final String OPENING_PROVISIONAL = "provivisional";
        final Map<String, FlatStop> products = new LinkedHashMap<>();
        BigDecimal totalDistance = BigDecimal.ZERO;
        Coordinates prevCoordinates = null;

        String firstStopId = null;
        String lastStopId = null;
        int index = 1;

        for (Day day : itinerary.getDays()) {
            for (Stop stop : day.getStops()) {
                List<String> errors = new ArrayList<>();
                FlatStop model = new FlatStop(stop);
                Coordinates coordinates = new Coordinates();
                String visitDuration = null;
                String location = null;
                model.setIndex(index++);
                FlatImage flatImage = null;

                if (stop.getImage() != null) {
                    Image cmsImage = stop.getImage();
                    if (cmsImage != null) {
                        //TODO Use imageFactory
                        flatImage = new FlatImage(cmsImage, request.getLocale());
                        checkImageErrors(flatImage, request.getLocale(), errors);
                        if (!(stop.getStopItem() instanceof DMSLink)) {
                            //TODO: Initialize Location Loader in the constructor Area
                            LocationObject locationObject = locationLoader.getLocation(cmsImage.getLocation(), request.getLocale());
                            if (locationObject != null) {
                                location = locationObject.getName();
                            }
                        }
                    }
                }

                if (stop.getStopItem() instanceof DMSLink) {
                    DMSLink dmsLink = (DMSLink) stop.getStopItem();

                    if (dmsLink.getProduct() == null) {
                        errors.add("The product's id  was not provided");
                        logger.warn(CommonUtilsService.contentIssue("The product's id was not provided for %s, Stop %s", itinerary.getName(), model.getIndex()));
                    } else {
                        JsonNode product = dmsData.productCard(dmsLink.getProduct(), request.getLocale());
                        if (product == null) {
                            errors.add("The product id does not match in the DMS");
                            logger.warn(CommonUtilsService.contentIssue("The product id does not match in the DMS for %s, Stop %s", itinerary.getName(), model.getIndex()));
                        } else {

                            FlatLink ctaLink = new FlatLink(bundle.getCtaLabel(dmsLink.getLabel(), request.getLocale()), product.get(URL).asText(), LinkType.INTERNAL);
                            model.setCtaLink(ctaLink);
                            if (product.has(ADDRESS)) {
                                JsonNode address = product.get(ADDRESS);
                                model.setAddress(address);
                                location = address.has(LOCATION) ? address.get(LOCATION).asText() : null;
                            }


                            model.setTimeToexplore(product.has(TIME_TO_EXPLORE) ? product.get(TIME_TO_EXPLORE).asText() : null);
                            if (product.has(TIME_TO_EXPLORE)) {
                                visitDuration = product.get(TIME_TO_EXPLORE).asText();
                            }

                            if (product.has(PRICE)) {
                                JsonNode price = product.get(PRICE);
                                model.setPrice(price.get(DISPLAY_PRICE).asText());
                            }

                            if (stop.getImage() == null && product.has(IMAGE)) {
                                JsonNode dmsImageList = product.get(IMAGE);
                                //TODO Use ImageFactory
                                flatImage = new FlatImage(dmsImageList.get(0), product.get(NAME).asText());
                            }

                            coordinates.setLatitude(product.get(LAT).asDouble());
                            coordinates.setLongitude(product.get(LON).asDouble());
                            model.setCoordinates(coordinates);

                            //TODO dmsUtils.getFacilities
                            model.setFacilities(getFacilities(product));

                            if (product.has(OPENING)) {
                                JsonNode opening = product.get(OPENING);
                                //TODO adjust the message to designs when ready
                                if ((opening.has(OPENING_STATE)) && (!opening.get(OPENING_STATE).asText().equalsIgnoreCase("unknown"))) {
                                    String openingMessage = opening.get(OPENING_PROVISIONAL).asBoolean() == false ? "Usually " : "Provisionally ";
                                    openingMessage = openingMessage + opening.get(OPENING_STATE).asText() + " " + opening.get(OPENING_DAY).asText();
                                    if ((opening.has(START_TIME)) && (opening.has(END_TIME))) {
                                        openingMessage = openingMessage + ": " + opening.get(START_TIME).asText() + "-" + opening.get(END_TIME).asText();
                                    }
                                    model.setOpen(openingMessage);
                                    model.setOpenLink(new FlatLink(bundle.getResourceBundle("itinerary", "stop.opening",
                                            request.getLocale()), ctaLink.getLink() + "#opening", null));
                                }
                            }

                        }
                    }
                } else if (stop.getStopItem() instanceof ItineraryExternalLink) {
                    ItineraryExternalLink externalLink = (ItineraryExternalLink) stop.getStopItem();
                    visitDuration = externalLink.getTimeToExplore();

                    if (externalLink.getExternalLink() != null) {
                        FlatLink ctaLink = new FlatLink(bundle.getCtaLabel(externalLink.getExternalLink().getLabel(), request.getLocale()),
                                externalLink.getExternalLink().getLink(), LinkType.EXTERNAL);
                        model.setCtaLink(ctaLink);
                    }

                    if (externalLink.getCoordinates() != null) {
                        coordinates.setLatitude(externalLink.getCoordinates().getLatitude());
                        coordinates.setLongitude(externalLink.getCoordinates().getLongitude());
                        model.setCoordinates(coordinates);
                    }

                } else {
                    errors.add("The product's id  was not provided");
                    logger.warn(CommonUtilsService.contentIssue("The product's id  was not provided for %s, Stop %s", itinerary.getName(), model.getIndex()));
                }


                lastStopId = model.getIdentifier();
                if (firstStopId == null) {
                    firstStopId = lastStopId;
                }
                if (model.getCoordinates() != null && flatImage!=null) {
                    flatImage.setCoordinates(model.getCoordinates());
                }
                model.setImage(flatImage);
                if (flatImage==null){
                    errors.add("An image should be provided for external links");
                }
                if (visitDuration != null) {
                    visitDuration = visitDuration.equalsIgnoreCase("1") ? visitDuration + " " + bundle.getResourceBundle("itinerary", "stop.hour", request.getLocale())
                            : visitDuration + " " + bundle.getResourceBundle("itinerary", "stop.hours", request.getLocale());
                    model.setTimeToexplore(visitDuration);
                }

                if (stop.getSubtitle() == null || stop.getSubtitle().isEmpty()) {
                    model.setSubTitle(location);
                } else {
                    model.setSubTitle(stop.getSubtitle());
                }
                model.setErrorMessages(errors);

                if (itinerary.getDistance() == 0) {
                    if (prevCoordinates != null && model.getCoordinates() != null) {
                        BigDecimal distancePrevStop = getDistanceStops(model, prevCoordinates);
                        totalDistance = totalDistance.add(distancePrevStop);
                        model.setDistance(distancePrevStop);
                    }
                    prevCoordinates = model.getCoordinates();
                }
                products.put(model.getIdentifier(), model);
            }
        }

        request.setAttribute(DISTANCE, totalDistance.compareTo(BigDecimal.ZERO) == 0 ? itinerary.getDistance() : totalDistance);
        request.setAttribute(STOPS_MAP, products);
        request.setAttribute(OTYML, addOTYML(itinerary, request.getLocale()));

        if (products.size() > 0) {
            request.setAttribute(FIRST_STOP_LOCATION, itinerary.getStart().isEmpty() ? products.get(firstStopId).getSubTitle() : itinerary.getStart());
            request.setAttribute(LAST_STOP_LOCATION, itinerary.getFinish().isEmpty() ? products.get(lastStopId).getSubTitle() : itinerary.getFinish());
        } else {
            request.setAttribute(FIRST_STOP_LOCATION, itinerary.getStart());
            request.setAttribute(LAST_STOP_LOCATION, itinerary.getFinish());
        }
    }

    /**
     * Method to calculate the distance between stops
     * y     * @param model the stop
     *
     * @return distance between stops
     */

    private BigDecimal getDistanceStops(FlatStop model, Coordinates prevCoordinates) {
        BigDecimal distance = CoordinateUtils.haversineDistance(new BigDecimal(prevCoordinates.getLatitude()), new BigDecimal(prevCoordinates.getLongitude()),
                new BigDecimal(model.getCoordinates().getLatitude()), new BigDecimal(model.getCoordinates().getLongitude()), true, "#,###,##0.0");

        if (distance != null) {
            model.setDistance(distance);
        }

        return distance;
    }

}
