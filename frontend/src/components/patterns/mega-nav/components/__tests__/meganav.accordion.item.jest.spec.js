import { shallowMount } from '@vue/test-utils';
import VsMegaNavAccordionItem from '../MegaNavAccordionItem';

const ctaLinkProp = 'https://www.visitscotland.com';
const ctaTextProp = 'All things to do';

const factoryShallowMount = () => shallowMount(VsMegaNavAccordionItem, {
    propsData: {
        controlId: 'accordion-item',
        title: 'Test Title',
        level: '1',
    },
    slots: {
        default: '<div class="accordion-item"></div>',
    },
});

describe('VsMegaNavAccordionItem', () => {
    it('should render a component with the data-test attribute `vs-mega-nav-accordion-item`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.attributes('data-test')).toBe('vs-mega-nav-accordion-item');
    });

    describe(':props', () => {
        it('returns a unique ID from the `title`', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.attributes('data-unique-id')).toBe('vs-mega-nav-test-title');
        });

        it('sets correct class for `level` provided', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.classes('vs-mega-nav-accordion-item--level-1')).toBe(true);
        });

        it('sets correct `controlId` with string provided with the unique id', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.attributes('controlid')).toBe('vs-mega-nav-accordion-item-vs-mega-nav-test-title-accordion-item');
        });

        it('does not show a cta link if no `ctaLink` or `ctaText` is provided', () => {
            const wrapper = factoryShallowMount();
            const ctaLink = wrapper.find('[data-test="vs-mega-nav-accordion-item__cta-link"]');
            expect(ctaLink.exists()).toBe(false);
        });

        it('does not show a cta link if `ctaLink` is provided but no `ctaText` is provided', async() => {
            const wrapper = factoryShallowMount();
            wrapper.setProps({
                ctaLink: ctaLinkProp,
            });
            await wrapper.vm.$nextTick();

            const ctaLink = wrapper.find('[data-test="vs-mega-nav-accordion-item__cta-link"]');
            expect(ctaLink.exists()).toBe(false);
        });

        it('does not show a cta link if `ctaText` is provided but no `ctaLink` is provided', async() => {
            const wrapper = factoryShallowMount();
            wrapper.setProps({
                ctaText: ctaTextProp,
            });
            await wrapper.vm.$nextTick();

            const ctaLink = wrapper.find('[data-test="vs-mega-nav-accordion-item__cta-link"]');
            expect(ctaLink.exists()).toBe(false);
        });

        it('shows a cta link if `ctaText` and `ctaLink` is provided', async() => {
            const wrapper = factoryShallowMount();
            wrapper.setProps({
                ctaText: ctaTextProp,
                ctaLink: ctaLinkProp,
            });
            await wrapper.vm.$nextTick();
            const ctaLink = wrapper.find('[data-test="vs-mega-nav-accordion-item__cta-link"]');

            expect(ctaLink.exists()).toBe(true);
            expect(ctaLink.text()).toBe(ctaTextProp);
            expect(ctaLink.attributes('href')).toBe(ctaLinkProp);
        });
    });

    describe(':slots', () => {
        it('renders a div element in the default slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.findAll('.accordion-item').length).toBe(1);
        });
    });
});
