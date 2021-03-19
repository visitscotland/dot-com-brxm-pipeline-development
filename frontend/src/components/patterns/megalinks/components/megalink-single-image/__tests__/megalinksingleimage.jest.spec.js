import { shallowMount } from '@vue/test-utils';
import VsMegalinkSingleImage from '../MegalinkSingleImage';

const factoryShallowMount = () => shallowMount(VsMegalinkSingleImage, {
    propsData: {
        alternate: false,
        title: 'The Single Image title',
        buttonLink: 'http://www.visitscotland.com',
        theme: 'dark',
    },
    slots: {
        vsSingleImageContent: '<p>This is the content for the single image component</p>',
        vsSingleImageLinks: '<li>This is just one link</li>',
        vsSingleImageButtonText: 'This is the button text',
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsMegalinkSingleImage', () => {
    describe(':props', () => {
        it('if the alternate prop is true, it should render an element with class "offset-lg-6"', async() => {
            await wrapper.setProps({
                alternate: true,
            });

            expect(wrapper.find('.megalink-single-image--alternate').exists()).toBe(true);
        });
        it('if the title prop is empty, it should not render a header element', async() => {
            await wrapper.setProps({
                title: '',
            });

            expect(wrapper.find('[data-test="megalink-single-image__title"]').exists()).toBe(false);
        });
        it('renders the correct theme class', () => {
            expect(wrapper.find('[data-test="megalink-single-image"]').classes()).toContain('megalink-single-image--dark');
        });
    });

    describe(':slots', () => {
        it('renders content inserted in a vsSingleImageContent slot', () => {
            expect(wrapper.find('[data-test="megalink-single-image__content"').html()).toContain('<p>This is the content for the single image component</p>');
        });

        it('renders content inserted in a vsSingleImageLinks slot', () => {
            expect(wrapper.find('[data-test="megalink-single-image__content"]').html()).toContain('<li>This is just one link</li>');
        });

        it('renders content inserted in a vsSingleImageButtonText slot', () => {
            expect(wrapper.find('[data-test="megalink-single-image__content"]').html()).toContain('This is the button text');
        });
    });
});
