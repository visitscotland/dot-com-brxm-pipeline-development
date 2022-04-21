import { mount } from '@vue/test-utils';
import moxios from 'moxios';
import VsMegaNavFeaturedEvent from '../MegaNavFeaturedEvent';

const id = '2573081';
const name = 'Harry Panto: Return to Frogwarts';
const category = [{
    id: 'theatreev',
    name: 'Theatre',
}];
const images = [{
    mediaType: 'PICTURE',
    mediaUrl: 'http://www.visitscotland.com/wsimgs/Events/397D6BF923226206F7290CC46A4017C72C38254D.jpg',
    default: true,
}];
const address = {
    line1: '33 King Street',
    city: 'Aberdeen',
    county: 'Aberdeen & Aberdeenshire',
    postCode: 'AB24 5AA',
    shortAddress: 'Aberdeen, Aberdeen & Aberdeenshire',
    streetAddress: '33 King Street',
    empty: false,
};
const description = 'Our young magicians are back! Join them as they fight the new mistress of darkness in this spellbinding tale of good versus evil.';
const productLink = {
    label: 'View Details',
    link: '/info/events/harry-panto-return-to-frogwarts-p2573081',
    type: 'INTERNAL',
};
const website = {
    label: 'View Event Details',
    link: '/info/events/harry-panto-return-to-frogwarts-p2573081',
    type: 'INTERNAL',
};
const price = {
    price: '£13.00',
    priceLabel: 'Price from',
    priceBasis: 'Per ticket',
};
const opening = {
    period: {
        label: 'Dates from',
        startDay: '22 Oct 2022',
        endDay: '23 Oct 2022',
    },
};
const eventVenue = 'Aberdeen Arts Centre';
const latitude = 57.14947;
const longitude = -2.09389;

const factoryMount = (slotData) => mount(VsMegaNavFeaturedEvent, {
    propsData: {
        sourceUrl: '/example/featured/event',
    },
    ...slotData,
});

describe('VsMegaNavFeaturedEvent', () => {
    beforeEach(() => {
        moxios.install();

        moxios.stubRequest('/example/featured/event', {
            status: 200,
            responseText: {
                data: {
                    products: [{
                        id,
                        name,
                        category,
                        images,
                        address,
                        description,
                        productLink,
                        website,
                        price,
                        opening,
                        eventVenue,
                        latitude,
                        longitude,
                    }],
                },
            },
        });
    });

    afterEach(() => {
        moxios.uninstall();
    });

    it('should render a component with the data-test attribute `vs-mega-nav-featured-event`', () => {
        const wrapper = factoryMount();

        return new Promise((resolve) => {
            moxios.wait(() => {
                expect(wrapper.find('[data-test="vs-mega-nav-featured-event"]').exists()).toBe(true);
                resolve();
            });
        });
    });

    describe(':data', () => {
        it('should render an image with the `images[0].mediaUrl` value from the api response', () => {
            const wrapper = factoryMount();

            return new Promise((resolve) => {
                moxios.wait(() => {
                    expect(wrapper.find('img').attributes('src')).toBe(images[0].mediaUrl);
                    resolve();
                });
            });
        });

        it('should render alt text with the `name` value from the api response', () => {
            const wrapper = factoryMount();

            return new Promise((resolve) => {
                moxios.wait(() => {
                    expect(wrapper.find('img').attributes('alt')).toBe(name);
                    resolve();
                });
            });
        });

        it('should render the content of the `name` value from the api response', () => {
            const wrapper = factoryMount();

            return new Promise((resolve) => {
                moxios.wait(() => {
                    expect(wrapper.text()).toContain(name);
                    resolve();
                });
            });
        });

        it('should render the content of the `address.shortAddress` value from the api response', () => {
            const wrapper = factoryMount();

            return new Promise((resolve) => {
                moxios.wait(() => {
                    expect(wrapper.text()).toContain(address.shortAddress);
                    resolve();
                });
            });
        });

        it('should render the content of the `opening.period.label` value from the api response', () => {
            const wrapper = factoryMount();

            return new Promise((resolve) => {
                moxios.wait(() => {
                    expect(wrapper.text()).toContain(opening.period.label);
                    resolve();
                });
            });
        });

        it('should render the content of the `opening.period.startDay` value from the api response', () => {
            const wrapper = factoryMount();

            return new Promise((resolve) => {
                moxios.wait(() => {
                    expect(wrapper.text()).toContain(opening.period.startDay);
                    resolve();
                });
            });
        });

        it('should render a link using the `productLink.link` value from the api response as href', () => {
            const wrapper = factoryMount();

            return new Promise((resolve) => {
                moxios.wait(() => {
                    expect(wrapper.find(`a[href="${ productLink.link }"]`).exists()).toBe(true);
                    resolve();
                });
            });
        });

        it('should render the content of the `productLink.label` value from the api response', () => {
            const wrapper = factoryMount();

            return new Promise((resolve) => {
                moxios.wait(() => {
                    expect(wrapper.text()).toContain(productLink.label);
                    resolve();
                });
            });
        });
    });
});
