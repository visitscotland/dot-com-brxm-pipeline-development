import { shallowMount } from '@vue/test-utils';

import VsPsrEmbed from '../PsrEmbed';

const factoryShallowMount = () => shallowMount(VsPsrEmbed, {
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

describe('VsPsrEmbed', () => {
    it('should render a div with the class `vs-psr-embed`', () => {
        wrapper = factoryShallowMount();

        expect(wrapper.element.tagName).toBe('DIV');
        expect(wrapper.classes('vs-psr-embed')).toBe(true);
    });

    describe(':props', () => {
        it('should render data attributes defined in props', async() => {
            await wrapper.vm.$nextTick();
            expect(wrapper.attributes('data-loc')).toBe('Edinburgh');
        });
    });
});
