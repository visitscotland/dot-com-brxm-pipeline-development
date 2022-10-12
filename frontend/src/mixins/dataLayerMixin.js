import dataLayerStore from '../stores/dataLayer.store';
import checkVendorLibrary from '../utils/check-vendor-library';
import {
    pageViewTemplate,
    externalLinkTemplate,
    internalLinkTemplate,
    videoTrackingTemplate,
    errorTemplate,
    menuNavigationTemplate,
    homePageLogoClickTemplate,
    formsTemplate,
    socialMediaExternalLinkTemplate,
} from '../utils/data-layer-templates';

/**
 * There is a general function to retrieve any value from the store:
 * "field_needed": dataLayerStore.getters.getValueFromKey("field_needed"),
 * example: "page_category_1": dataLayerStore.getters.getValueFromKey("page_category_1")
 */

const dataLayerMixin = {
    computed: {
        // Retrieving page url from the page store created by:
        // dataLayer.store.js (Central Store)
        // TagManagerWrapper.vue (Global component that reads and updates the store)
        pageUrl() {
            return dataLayerStore.getters.getPageUrl;
        },
    },
    methods: {
        // This function matches values passed as an object
        // To the template listed on data-layer-template.js
        // And return any value not matched as undefined
        templateFiller(template, values) {
            // Create an empty object
            const obj = {
            };

            // Check every element on the template
            template.forEach((element) => {
                // Push the key from the template to the object created before
                obj[element] = '';

                // If there is a key on the object passed by the user that
                // matches a key from the template

                // Then use the user given value otherwise return it as undefined

                // This also ignores any key passed by the user that doesn't match
                // a key from the template
                obj[element] = values[element] || undefined;
            });

            // Return an object ready to be pushed to the data-layer
            return obj;
        },
        createDataLayerObject(type, event, href) {
            let eventName;
            let tagName;
            let templateValues;
            let fullTemplate;
            let dataLayerData;

            switch (type) {
            case 'pageViewTemplateDataEvent':
                eventName = 'page_view';

                templateValues = {
                    event: eventName,
                };

                fullTemplate = this.compileFullTemplate(templateValues);

                // Running the values and the template trough the templateFiller() function
                // This will make sure that the values are added on the right place
                // And if any value was not found then it will return as undefined
                // (as per iProspect request)
                dataLayerData = this.templateFiller(pageViewTemplate, fullTemplate);
                break;

            case 'menuNavigationDataEvent':
                eventName = 'menu_navigation';

                templateValues = {
                    event: eventName,
                    click_text: event.target.text.trim(),
                    click_URL: href,
                };

                fullTemplate = this.compileFullTemplate(templateValues);
                dataLayerData = this.templateFiller(menuNavigationTemplate, fullTemplate);
                break;

            case 'socialMediaExternalLinkDataEvent':
                eventName = 'social_media_footer';
                tagName = 'VS - GA - Social Media External Link';

                templateValues = {
                    event: eventName,
                    tag_name: tagName,
                    click_URL: href,
                };

                fullTemplate = this.compileFullTemplate(templateValues);
                dataLayerData = this.templateFiller(socialMediaExternalLinkTemplate, fullTemplate);
                break;

            case 'homePageLogoClickDataEvent':
                eventName = 'homepage_logo_click';
                tagName = 'VS - GA - Homepage Logo Click';

                templateValues = {
                    event: eventName,
                    tag_name: tagName,
                };

                fullTemplate = this.compileFullTemplate(templateValues);
                dataLayerData = this.templateFiller(homePageLogoClickTemplate, fullTemplate);
                break;

            case 'externalLinkDataEvent':
                eventName = 'external_link';
                tagName = 'VS - GA - External Link';

                templateValues = {
                    event: eventName,
                    tag_name: tagName,
                    click_text: this.targetText(event),
                    click_URL: event.target.href,
                };

                fullTemplate = this.compileFullTemplate(templateValues);
                dataLayerData = this.templateFiller(externalLinkTemplate, fullTemplate);
                break;

            case 'internalLinkDataEvent':
                eventName = 'internal_link';
                tagName = 'VS - GA - Internal Link';

                templateValues = {
                    event: eventName,
                    tag_name: tagName,
                    click_text: event.target.text.trim(),
                    click_URL: href,
                };

                fullTemplate = this.compileFullTemplate(templateValues);
                dataLayerData = this.templateFiller(internalLinkTemplate, fullTemplate);
                break;

            case 'formsDataEvent':
                eventName = 'forms';
                tagName = 'VS - GA - Forms';

                templateValues = {
                    event: eventName,
                    tag_name: tagName,
                    form_status: 'form_submitted',
                };

                fullTemplate = this.compileFullTemplate(templateValues);
                dataLayerData = this.templateFiller(formsTemplate, fullTemplate);
                break;

            case 'errorDataEvent':
                eventName = 'errors';
                tagName = 'VS - GA - Errors';

                templateValues = {
                    event: eventName,
                    tag_name: tagName,
                    error_type: event.error_type,
                    error_details: event.error_details,
                };

                fullTemplate = this.compileFullTemplate(templateValues);
                dataLayerData = this.templateFiller(errorTemplate, fullTemplate);
                break;

            case 'videoTrackingDataEvent':
                eventName = 'video_tracking';
                tagName = 'VS - GA - Video Tracking';

                templateValues = {
                    event: eventName,
                    tag_name: tagName,
                    video_status: event.status,
                    video_title: event.title || '',
                    video_percent: event.percent || 0,
                };

                fullTemplate = this.compileFullTemplate(templateValues);
                dataLayerData = this.templateFiller(videoTrackingTemplate, fullTemplate);

                break;

            default:
            }

            this.pushToDataLayer(dataLayerData);
        },

        // pageViewTemplateDataEvent() {
        //     const eventName = 'page_view';

        //     const templateValues = {
        //         event: eventName,
        //     };

        //     const fullTemplate = this.compileFullTemplate(templateValues);

        //     // Running the values and the template trough the templateFiller() function
        //     // This will make sure that the values are added on the right place
        //     // And if any value was not found then it will return as undefined
        //     // (as per iProspect request)
        //     const pageView = this.templateFiller(pageViewTemplate, fullTemplate);

        //     this.pushToDataLayer(pageView);
        // },
        // menuNavigationDataEvent(event) {
        //     const eventName = 'menu_navigation';

        //     const templateValues = {
        //         event: eventName,
        //         click_text: event.target.text.trim(),
        //         click_URL: href,
        //     };

        //     const fullTemplate = this.compileFullTemplate(templateValues);
        //     const menuNavigation = this.templateFiller(menuNavigationTemplate, fullTemplate);
        //     this.pushToDataLayer(menuNavigation);
        // },
        // newsletterDataEvent(event) {
        //     const eventName = "newsletter"
        // },
        // shareDataEvent(event) {
        //     const eventName = "share"
        // },
        // socialMediaExternalLinkDataEvent(href) {
        //     const eventName = 'social_media_external_link';

        //     const templateValues = {
        //         event: eventName,
        //         click_URL: href,
        //     };

        //     const fullTemplate =
        //          this.compileFullTemplate(templateValues);
        //     const socialClick =
        // this.templateFiller(socialMediaExternalLinkTemplate, fullTemplate);
        //     this.pushToDataLayer(socialClick);
        // },
        // homePageLogoClickDataEvent() {
        //     const eventName = 'homepage_logo_click';

        //     const templateValues = {
        //         event: eventName,
        //     };

        //     const fullTemplate = this.compileFullTemplate(templateValues);
        //     const homePageLogoClick =
        //          this.templateFiller(homePageLogoClickTemplate, fullTemplate);
        //     this.pushToDataLayer(homePageLogoClick);
        // },
        // // videoTrackingDataEvent(event) {
        // //     const eventName = "video_tracking"
        // // },
        // externalLinkDataEvent(event) {
        //     // Fixed values
        //     const eventName = 'external_link';

        //     const templateValues = {
        //         event: eventName,
        //         click_text: event.target.text.trim(),
        //         click_URL: href,
        //     };

        //     const fullTemplate = this.compileFullTemplate(templateValues);

        //     // Running the values and the template trough the templateFiller() function
        //     // This will make sure that the values are added on the right place
        //     // And if any value was not found then it will return as undefined
        //     // (as per iProspect request)
        //     const externalLink = this.templateFiller(externalLinkTemplate, fullTemplate);

        //     // After that we just need to push the object returned to the data layer
        //     this.pushToDataLayer(externalLink);
        // },
        // internalLinkDataEvent(event) {
        //     const eventName = 'internal_link';

        //     const templateValues = {
        //         event: eventName,
        //         click_text: event.target.text.trim(),
        //         click_URL: href,
        //     };

        //     const fullTemplate = this.compileFullTemplate(templateValues);

        //     const internalLink = this.templateFiller(internalLinkTemplate, fullTemplate);

        //     this.pushToDataLayer(internalLink);
        // },
        // internalNavigation(event) {
        //     const eventName = 'internal_navigation'
        // },
        // errorDataEvent(event) {
        //     const eventName = 'errors';
        //     const tagName = 'VS - GA - Errors';

        //     const templateValues = {
        //         event: eventName,
        //         tag_name: tagName,
        //         error_type: event.error_type,
        //         error_details: event.error_details,
        //         click_text: this.targetText(event),
        //         click_URL: event.target.href,
        //     };

        //     const fullTemplate = this.compileFullTemplate(templateValues);
        //     const errorData = this.templateFiller(errorTemplate, fullTemplate);

        //     this.pushToDataLayer(errorData);
        // },
        // mapInteractionDataTemplate(event) {
        //     const eventName = 'map_interaction'
        // },
        // cannedSearchDataTemplate(event) {
        //     const eventName = 'canned_search'
        // },
        // formsDataEvent() {
        //     const eventName = 'forms';

        //     const templateValues = {
        //         event: eventName,
        //         form_status: 'form_viewed',
        //     };

        //     const fullTemplate = this.compileFullTemplate(templateValues);
        //     const formEvent = this.templateFiller(formsTemplate, fullTemplate);
        //     this.pushToDataLayer(formEvent);
        // },

        returnIsoDate() {
            const date = new Date(Date.now());
            return date.toISOString();
        },
        pushToDataLayer(object) {
            checkVendorLibrary('dataLayer', () => {
                // eslint-disable-next-line
                dataLayer.push(object);
            });
        },
        compileFullTemplate(templateValues) {
            const storeValues = dataLayerStore.getters.getAllGTMValues;
            const fullTemplate = {
                ...storeValues,
                ...templateValues,
            };

            fullTemplate.hit_timestamp = this.returnIsoDate();

            return fullTemplate;
        },
        targetText(event) {
            let text;

            if (event.target.text) {
                text = event.target.text.trim();
            } else {
                text = '';
            }

            return text;
        },
    },
};

export default dataLayerMixin;
