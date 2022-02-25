import { mount } from '@vue/test-utils';
import VsCannedSearchPrice from '../CannedSearchPrice';

const priceIntro = 'Price from';
const price = '£110';
const priceOutro = 'Per room per night (breakfast inc.)';

const factoryShallowMount = (propsData) => mount(VsCannedSearchPrice, {
    propsData: {
        priceIntro,
        price,
        priceOutro,
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsCannedSearchPrice', () => {
    describe(':props', () => {
        it('should render the content of the `priceIntro` property', () => {
            expect(wrapper.html()).toContain(priceIntro);
        });

        it('should render the content of the `price` property', () => {
            expect(wrapper.html()).toContain(price);
        });

        it('should render the content of the `priceOutro` property', () => {
            expect(wrapper.html()).toContain(priceOutro);
        });
    });
});
