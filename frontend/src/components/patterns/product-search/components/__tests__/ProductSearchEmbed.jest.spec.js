import { shallowMount } from '@vue/test-utils';

import VsProductSearchEmbed from '../ProductSearchEmbed';

const factoryShallowMount = () => shallowMount(VsProductSearchEmbed, {
    propsData: {
        config: [{
            loc: 'Edinburgh',
        }],
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsProductSearchEmbed', () => {
    it('should render a div with the class `vs-product-search-embed`', () => {
        wrapper = factoryShallowMount();

        expect(wrapper.element.tagName).toBe('DIV');
        expect(wrapper.classes('vs-product-search-embed')).toBe(true);
    });

    describe(':props', () => {
        it('should render data attributes defined in props', async() => {
            await wrapper.vm.$nextTick();
            expect(wrapper.attributes('data-loc')).toBe('Edinburgh');
        });
    });
});
