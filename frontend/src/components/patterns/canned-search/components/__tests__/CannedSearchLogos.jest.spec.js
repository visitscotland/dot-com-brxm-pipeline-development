import { mount } from '@vue/test-utils';
import VsCannedSearchLogos from '../CannedSearchLogos';

const goodToGoLogo = {
    id: 'goodToGo',
    name: 'Good to go',
    image: 'https://www.visitscotland.com/cms-images/logos/goodToGo.png',
};
const safeTravelsLogo = {
    id: 'SafeTravels',
    name: 'Safe Travels',
    image: 'https://www.visitscotland.com/cms-images/logos/WTTC-SafeTravels.png',
};
const awards = [
    {
        id: 'qatasteourbest',
        name: 'Taste Our Best',
        image: 'http://visitscotlandpreview.nmdemo.net/wsimgs/awards/TOB-updated-white-back_286148508_714060445.jpg',
    },
    {
        id: 'qag2s',
        name: 'Green Tourism Silver',
        image: 'http://visitscotlandpreview.nmdemo.net/wsimgs/awards/GT_silver_200x200_275519812.png',
        type: 'GREEN_TOURISM',
    },
];

const factoryMount = (propsData) => mount(VsCannedSearchLogos, {
    propsData: {
        goodToGoLogo,
        safeTravelsLogo,
        awards,
        ...propsData,
    },
});

/* eslint-disable */
const originalWarn = console.warn.bind(console.warn);

beforeAll(() => {
    console.warn = (msg) => !msg.toString().includes('tooltip - The provided target is no valid HTML element.') && originalWarn(msg);
});

afterAll(() => {
    console.warn = originalWarn;
});
/* eslint-enable */

let wrapper;
beforeEach(() => {
    wrapper = factoryMount();
});

describe('VsCannedSearchLogos', () => {
    describe(':props', () => {
        it('should render a logo for each entry in the `awards` property', () => {
            expect(wrapper.findAll('[data-test="vs-canned-search-logos__award-logo"]').length).toBe(awards.length);
        });
    });
});
