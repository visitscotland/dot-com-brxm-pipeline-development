package com.visitscotland.brxm.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.hippobeans.capabilities.Linkable;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.model.Coordinates;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.dms.LocationLoader;
import com.visitscotland.brxm.model.FlatImage;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.model.megalinks.*;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.services.CommonUtilsService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.utils.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class LinkModulesFactory {

    private static final Logger logger = LoggerFactory.getLogger(LinkModulesFactory.class);

    public final static int MAX_ITEMS = 6;
    public final static String HORIZONTAL_LAYOUT = "Horizontal";

    private final static String IMAGE = "images";

    private final HippoUtilsService utils;
    private final DMSDataService dmsData;
    private final LinkService linkService;
    private final ResourceBundleService bundle;
    private final LocationLoader locationLoader;
    private final ImageFactory imageFactory;

    public LinkModulesFactory(HippoUtilsService utils, DMSDataService dmsData, LinkService linkService, ResourceBundleService bundle , LocationLoader locationLoader, ImageFactory imageFactory) {
        this.utils = utils;
        this.dmsData = dmsData;
        this.linkService = linkService;
        this.bundle = bundle;
        this.locationLoader = locationLoader;
        this.imageFactory = imageFactory;
    }

    public LinksModule<?> getMegalinkModule(Megalinks doc, Locale locale) {
        if (doc.getLayout()!= null && doc.getLayout().equalsIgnoreCase("list") || doc.getMegalinkItems().size() > MAX_ITEMS) {
            return listLayout(doc, locale) ;
        } else if (doc.getLayout()!= null && doc.getLayout().contains(HORIZONTAL_LAYOUT)) {
            return horizontalListLayout (doc, locale);
        }else if (doc.getSingleImageModule() != null) {
            return singleImageLayout(doc, locale);
        } else {
            return multiImageLayout(doc, locale);
        }
    }

    /**
     * Converts a Megalinks document into a {@code ListLinksModule} Object for easier the consumption of the data on the front
     * end.
     *
     * @param doc    Megalink document to be converted.
     * @param locale Locale of the request
     * @return {@code ListLinksModule} containing the relevant information from the Megalinks document
     */
    public ListLinksModule listLayout(Megalinks doc, Locale locale) {
        ListLinksModule ll = new ListLinksModule();
        populateCommonFields(ll, doc, locale);

        ll.setTeaserVisible(doc.getTeaserVisible());
        ll.setLinks(convertToEnhancedLinks(doc.getMegalinkItems(), locale, false));
        //TODO: Add or remove depending on decision 24/11/2020. This warning should be a content guideline? If warning, add  tests.
        /*List<String> warnings =  new ArrayList<>();
        if(ll.getLinks().size()==1){
            warnings.add("For list layout is recommended to have t least 2 links");
        }
        ll.setErrorMessages(warnings);*/
        return ll;
    }

    public HorizontalListLinksModule horizontalListLayout(Megalinks doc, Locale locale) {
        HorizontalListLinksModule ll = new HorizontalListLinksModule();
        populateCommonFields(ll, doc, locale);

        ll.setTeaserVisible(doc.getTeaserVisible());
        ll.setLinks(convertToEnhancedLinks(doc.getMegalinkItems(), locale, false));

        return ll;
    }

    public HorizontalListLinksModule horizontalListLayout(OTYML doc, Locale locale) {
        HorizontalListLinksModule target = new HorizontalListLinksModule();
        target.setTitle(Contract.isEmpty(doc.getTitle())? (bundle.getResourceBundle("otyml", "otyml.title.default", locale ,true)): doc.getTitle());
        target.setIntroduction(doc.getIntroduction());
        target.setLinks(convertToEnhancedLinks(doc.getMegalinkItems(), locale,true));

        return target;
    }

    /**
     * Converts a Megalinks document into a {@code SingleImageLinksModule} Object for easier the consumption of the data on the front
     * end.
     *
     * @param doc    Megalink document to be converted.
     * @param locale Locale of the request
     * @return {@code SingleImageLinksModule} containing the relevant information from the Megalinks document
     */
    public SingleImageLinksModule singleImageLayout(Megalinks doc, Locale locale) {
        SingleImageLinksModule sil = new SingleImageLinksModule();
        populateCommonFields(sil, doc, locale);

        sil.setInnerTitle(doc.getSingleImageModule().getTitle());
        sil.setInnerIntroduction(doc.getSingleImageModule().getIntroduction());
        sil.setImage(createFlatImage(doc.getSingleImageModule().getImage(), locale));
        sil.setLinks(convertToFlatLinks(doc.getMegalinkItems(), locale));

        return sil;
    }

    /**
     * Converts a Megalinks document into a MultiImageLinksModule Object for easier the consumption of the data on the front
     * end.
     * <p>
     * The number of featured items marked on the document might change depending on the following rules:
     * <ul>
     * <li>Megalinks with 2 or less featured items won't have featured items</li>
     * <li>Megalinks with 3 items might have up to one featured items</li>
     * <li>Megalinks with 4 or more items will have between 1 and 2 featured items</li>
     * <li>When the number of featured items exceed what is expected, only the first items will remain as Featured</li>
     * <li>When the number of featured items is inferior to the expected, the first non-featured items will be promoted
     * to featured items</li>
     * </ul>
     *
     * @param doc    Megalink document to be converted.
     * @param locale Locale of the request
     * @return MultiImageLinksModule containing the relevant information from the Megalinks document
     */
    public MultiImageLinksModule multiImageLayout(Megalinks doc, Locale locale) {
        //TODO: Add or remove depending on decision 24/11/2020. This warning should be a content guideline? If warning, add  tests.
        //List<String> warnings =  new ArrayList<>();
        MultiImageLinksModule fl = new MultiImageLinksModule();
        populateCommonFields(fl, doc, locale);
        fl.setTeaserVisible(doc.getTeaserVisible());

        fl.setLinksSize(doc.getMegalinkItems().size());
        fl.setLinks(convertToEnhancedLinks(doc.getMegalinkItems(), locale,false));

        if (fl.getLinks().size() == 1) {
            //If the megalinks only have one item, that one is featured
            fl.setFeaturedLinks(fl.getLinks());
            fl.setLinks(Collections.EMPTY_LIST);
        } else if (fl.getLinks().size() > 2) {
            //For 3 links the maximum of 1 featured item.  From 4 on the maximum is 2 featured items.
            fl.setFeaturedLinks(fl.getLinks().stream()
                    .filter(link -> link.isFeatured())
                    .limit(fl.getLinks().size() == 3 ? 1 : 2)
                    .collect(Collectors.toCollection(ArrayList::new)));

            //When there is more than 3 items and no featured item the first item is promoted as featured.
            if (fl.getFeaturedLinks().size() == 0 && fl.getLinks().size() > 3) {
                fl.getFeaturedLinks().add(fl.getLinks().get(0));
                //TODO: Add or remove depending on decision 24/11/2020. This warning should be a content guideline? If warning, add  tests.
                //warnings.add("No featured item provided, first link will be selected as featured");
            }

            //Links added to the Featured list MUST be removed from the original list
            fl.getLinks().removeAll(fl.getFeaturedLinks());
            //TODO: Add or remove depending on decision 24/11/2020. This warning should be a content guideline? If warning, add  tests.
            //fl.setErrorMessages(warnings);
        } else {
            fl.setFeaturedLinks(Collections.EMPTY_LIST);
        }

        return fl;
    }

    /**
     * Populate the common fields among all layouts
     *
     * @param target target module to be populated
     * @param doc    Megalinks document with the data source
     * @param locale consumer language.
     */
    private void populateCommonFields(LinksModule<?> target, Megalinks doc, Locale locale) {
        target.setHippoBean(doc);
        target.setTitle(doc.getTitle());
        target.setIntroduction(doc.getIntroduction());

        if (doc.getProductItem() != null) {
            target.setCta(linkService.createLink(locale, doc.getProductItem()));
        }
    }

    /**
     * Converts the list of {@code MegalinksItem} into  a list of plain {@code FlatLink }
     */
    List<FlatLink> convertToFlatLinks(List<MegalinkItem> items, Locale locale) {
        List<FlatLink> links = new ArrayList<>();
        for (MegalinkItem item : items) {
            if (item.getLink() == null) {
                CommonUtilsService.contentIssue("The module %s contains a link without any reference", item.getPath());
            } else if (item.getLink() instanceof Page) {
                links.add(new FlatLink(((Page) item.getLink()).getTitle(), utils.createUrl(item.getLink()), LinkType.INTERNAL));
            } else if (item.getLink() instanceof SharedLink) {
                JsonNode node = getNodeFromSharedLink((SharedLink) item.getLink(), locale);
                FlatLink link = new FlatLink();
                link.setLabel(((SharedLink) item.getLink()).getTitle());
                link.setLink(linkService.getPlainLink((SharedLink) item.getLink(), node));
                link.setType(linkService.getType(link.getLink()));
                links.add(link);
            } else {
                CommonUtilsService.contentIssue("The module %s is pointing to a document of type %s which cannot be rendered as a page", item.getPath(), item.getLink().getClass().getSimpleName());
            }
        }
        return links;
    }

    /**
     * Converts the list of {@code MegalinksItem} into  a list of {@code EnhancedLink }
     */
    List<EnhancedLink> convertToEnhancedLinks(List<MegalinkItem> items, Locale locale, boolean addCategory) {
        List<EnhancedLink> links = new ArrayList<>();
        for (MegalinkItem item : items) {
            if (item.getLink() == null) {
                CommonUtilsService.contentIssue("The module %s contains a link without any reference", item.getPath());
            }else if (item.getLink() instanceof Linkable) {
                EnhancedLink link = createEnhancedLink((Linkable) item.getLink(), locale, addCategory);
                if (link != null) {
                    link.setFeatured(item.getFeature());
                    links.add(link);
                }
            } else {
                CommonUtilsService.contentIssue("The module %s is pointing to a document of type %s which cannot be rendered as a page", item.getPath(), item.getLink().getClass().getSimpleName());
            }
        }
        return links;
    }

    public EnhancedLink createEnhancedLink(Linkable linkable, Locale locale, boolean addCategory) {
        EnhancedLink link = new EnhancedLink();
        link.setTeaser(linkable.getTeaser());
        link.setLabel(linkable.getTitle());

        if (linkable.getImage() != null) {
            link.setImage(createFlatImage(linkable.getImage(), locale));
        }

        if (linkable instanceof Page) {
            link.setType(LinkType.INTERNAL);
            if (linkable instanceof Itinerary) {
                link.setItineraryTransport(((Itinerary) linkable).getTransports()[0]);
                link.setItineraryDays(((Itinerary) linkable).getDays().size());
            }
            link.setLink(utils.createUrl((Page) linkable));
            link.setType(LinkType.INTERNAL);
        } else if (linkable instanceof SharedLink) {
            JsonNode product = getNodeFromSharedLink((SharedLink) linkable, locale);
            SharedLink sharedLink = (SharedLink) linkable;
            if (link.getImage() == null && product != null && product.has(IMAGE)) {
                //TODO Propagate the error messages
                link.setImage(imageFactory.createImage(product, null));
            }
            if (((SharedLink) linkable).getLinkType() instanceof ExternalDocument){
                ExternalDocument externalDocument = (ExternalDocument)sharedLink.getLinkType();
                String extension = externalDocument.getLink().substring(externalDocument.getLink().lastIndexOf(".") + 1).toUpperCase();
                String downloadLabel = bundle.getResourceBundle("essentials.global", "label.download", locale ,true);
                link.setLabel(linkable.getTitle()+"("+downloadLabel+" "+extension+" " + externalDocument.getSize()+externalDocument.getBytes() + ")");
                link.setType(LinkType.DOWNLOAD);
                if (addCategory) {
                    link.setCategory(externalDocument.getCategory());
                }
            }
            link.setLink(linkService.getPlainLink((SharedLink) linkable, product));
            if (link.getType() == null) {
                link.setType(linkService.getType(link.getLink()));
            }
        } else {
            logger.warn(String.format("The type %s was not expected and will be skipped", linkable.getClass().getSimpleName()));
            return null;
        }

        if (addCategory && link.getLink()!= null && link.getCategory()==null){
            link.setCategory(linkService.getLinkCategory (link.getLink(),locale));
        }
        if (link.getImage() == null) {
            CommonUtilsService.contentIssue("The link to %s does not have an image but it is expecting one", linkable);
        }

        return link;
    }


    /**
     * Query the DMSDataService and extract the information about the product as a {@code JsonNode}
     *
     * @param link SharedLink where the DMS product (ID) is defined
     * @param locale User language to consume DMS texts such a category, location, facilities...
     * @return JSON with DMS product information to create the card or null if the product does not exist
     */
    private JsonNode getNodeFromSharedLink(SharedLink link, Locale locale) {
        if (link.getLinkType() instanceof DMSLink) {
            return dmsData.productCard(((DMSLink) link.getLinkType()).getProduct(), locale);
        }
        return null;
    }

    /**
     * Create a localized FlatImage from an Image Object
     *
     * @param img    Image Object
     * @param locale User language to localize Image texts such as the caption
     * @return flat image to be consumed by FED team
     */
    private FlatImage createFlatImage(Image img, Locale locale) {
        FlatImage flatImage = new FlatImage(img, locale);
        LocationObject locationObject = getLocation(img.getLocation(), locale);
        if (locationObject != null) {
            flatImage.setCoordinates(new Coordinates(locationObject.getLatitude(), locationObject.getLongitude()));
        }

        return flatImage;
    }

    //TODO convert into a service
    LocationObject getLocation(String location, Locale locale) {
        return locationLoader.getLocation(location, locale);
    }

}
