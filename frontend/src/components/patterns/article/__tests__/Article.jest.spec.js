import { shallowMount } from '@vue/test-utils';

import VsArticle from '../Article';

const slotContent = 'Article Content';

const factoryShallowMount = (propsData) => shallowMount(VsArticle, {
    propsData: {
        ...propsData,
    },
    slots: {
        default: slotContent,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsArticle', () => {
    it('should render a component with the data-test attribute `vs-article`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-article');
    });

    describe(':props', () => {
        it('should accept and render a `tag` property', async() => {
            await wrapper.setProps({
                tag: 'test-tag',
            });

            expect(wrapper.attributes('tag')).toBe('test-tag');
        });
    });

    describe(':slots', () => {
        it('renders content inserted into the default `slot`', () => {
            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
