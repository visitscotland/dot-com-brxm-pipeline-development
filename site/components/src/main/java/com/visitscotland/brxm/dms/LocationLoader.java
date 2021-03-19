package com.visitscotland.brxm.dms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.utils.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class LocationLoader {

    private static final Logger logger = LoggerFactory.getLogger(LocationLoader.class);

    private static LocationLoader instance;

    private final Map<Language, Map<String, LocationObject>> locations = new HashMap<>();

    private final Map<String, String> locationToId = new HashMap<>();

    private final DMSProxy proxy;

    public LocationLoader(DMSProxy proxy){
        this.proxy = proxy;
    }

    /**
     * Initialize maps
     */
    private void validateMaps() {
        synchronized (LocationLoader.class) {
            if (locationToId.size() == 0) {
                for (Language lang : Language.values()) {
                    Map<String, LocationObject> locationsMap = new HashMap<>();
                    try {
                        List<LocationObject> locationList = deserialize(request(lang.getLocale()));

                        //if the locationToId map is empty, and the locale is null. Both lists are populated simultaneously
                        if (locationToId.size() == 0 && lang == Language.ENGLISH) {
                            for (LocationObject location : locationList) {
                                locationToId.put(location.getName(), location.getId());
                                locationsMap.put(location.getId(), location);
                            }
                        } else {
                            for (LocationObject location : locationList) {
                                locationsMap.put(location.getId(), location);
                            }
                        }

                    } catch (IOException e) {
                        logger.warn("Location List couldn't been loaded for the locale {}", lang.getLocale());
                    } catch (Exception e) {
                        logger.error("Unexpected exception ", e);
                    }

                    locations.put(lang, locationsMap);
                }
            }
        }
    }

    /**
     * TODO: This is used by DMSLocationListProvider. Fix that usage.
     *
     * @deprecated use SpringContext.getBean (LocationLoader.class) instead
     */
    @Deprecated
    public static LocationLoader getInstance(){
        if (instance == null){
            instance = VsComponentManager.get(LocationLoader.class);
        }
        return instance;
    }

    private Map<String, LocationObject> getLocations(Language language){
        validateMaps();

        return locations.get(language);
    }

    public LocationObject getLocation(String location, Locale locale){
        return getLocations(Language.getLanguageForLocale(locale)).get(locationToId.get(location));
    }



    /**
     *
     * @param levels
     * @return
     */
    public List<LocationObject> getLocationsByLevel(String... levels){
        List<LocationObject> locationList = new ArrayList<>();
        for (LocationObject obj : getLocations(Language.ENGLISH).values()){
            if (levels!=null && levels.length>0){
                for (String level : levels){
                    if (obj.getTypes().contains(level)){
                        locationList.add(obj);
                        break;
                    }
                }
            }else{
                locationList.add(obj);
            }
        }
        if (locationList.isEmpty()){
            logger.warn("No objects matched with the types. It is possible that the types weren't loaded from the endpoint.");
        }

        Collections.sort(locationList, Comparator.comparing(LocationObject::getName));

        return  locationList;
    }

    private void clear(){
        locationToId.clear();
        locations.clear();
    }

    /**
     * Request the resource taking into account the language.
     *
     * @param locale: A specific locale for the fragment or null if the locale is English (default locale)
     *
     * @return HTML fragment according to the type and the locale
     */
    private String request(Locale locale){
        //TODO Change the level to add a polygon (for destinations pages)
        if (locale == null){
            return proxy.request(DMSConstants.META_LOCATIONS);
        } else {
            return proxy.request(DMSConstants.META_LOCATIONS, locale);
        }
    }

    /**
     * Deserialize the List of elements in a list of Locations
     * @param data
     * @return
     * @throws IOException
     */
    private List<LocationObject> deserialize(String data) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode dataObject = jsonMapper.readTree(data);
        List<LocationObject> objects = new ArrayList<>();

        if (!dataObject.has("data")){
            throw new IOException("No data field found");
        }

        for (JsonNode elm: dataObject.get("data")){
            objects.add(jsonMapper.readValue(elm.toString(), LocationObject.class));
        }
        return objects;
    }
}