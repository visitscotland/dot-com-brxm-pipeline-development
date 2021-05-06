import { shallowMount } from '@vue/test-utils';
import VsMegaNavListItem from '../MegaNavListItem';

const factoryShallowMount = (propsData) => shallowMount(VsMegaNavListItem, {
    propsData: {
        ...propsData,
    },
    slots: {
        default: 'Attractions',
    },
});

describe('VsMegaNavListItem', () => {
    it('should render a component with the data-test attribute `vs-mega-nav-list-item`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.attributes('data-test')).toBe('vs-mega-nav-list-item');
    });

    it('should render list item with class `vs-mega-nav-list-item__heading` by default', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.classes('vs-mega-nav-list-item__heading')).toBe(true);
    });

    it('should render list item without class `vs-mega-nav-list-item__subheading-link` by default', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.classes('vs-mega-nav-list-item__subheading-link')).toBe(false);
    });

    it('should render link with role `heading` by default', () => {
        const wrapper = factoryShallowMount();
        const link = wrapper.find('[data-test="vs-mega-nav-list-item"]').find('vslink-stub');
        expect(link.attributes('role')).toBe('heading');
    });

    it('should render link with href `#` by default', () => {
        const wrapper = factoryShallowMount();
        const link = wrapper.find('[data-test="vs-mega-nav-list-item"]').find('vslink-stub');
        expect(link.attributes('href')).toBe('#');
    });

    describe(':props', () => {
        it('should render list item with class `vs-mega-nav-list-item__link` when href is passed', () => {
            const wrapper = factoryShallowMount({
                href: 'http://visitscotland.com/attractions',
            });
            expect(wrapper.classes('vs-mega-nav-list-item__link')).toBe(true);
        });

        it('should render list item without class `vs-mega-nav-list-item__heading` when href is passed', () => {
            const wrapper = factoryShallowMount({
                href: 'http://visitscotland.com/attractions',
            });
            expect(wrapper.classes('vs-mega-nav-list-item__heading')).toBe(false);
        });

        it('should render link with correct URL when href is passed in', () => {
            const wrapper = factoryShallowMount({
                href: 'http://visitscotland.com/attractions',
            });
            const link = wrapper.find('[data-test="vs-mega-nav-list-item"]').find('vslink-stub');
            expect(link.attributes('href')).toBe('http://visitscotland.com/attractions');
        });

        it('should render link with `menuitem` role attribute when href is passed in', () => {
            const wrapper = factoryShallowMount({
                href: 'http://visitscotland.com/attractions',
            });
            const link = wrapper.find('[data-test="vs-mega-nav-list-item"]').find('vslink-stub');
            expect(link.attributes('role')).toBe('menuitem');
        });

        it('should render list item with class `vs-mega-nav-list-item__subheading-link` when subheadingLink is true', () => {
            const wrapper = factoryShallowMount({
                subheadingLink: true,
            });
            expect(wrapper.classes('vs-mega-nav-list-item__subheading-link')).toBe(true);
        });
    });

    describe(':slots', () => {
        it('renders link content in the default slot', () => {
            const wrapper = factoryShallowMount();
            const link = wrapper.find('[data-test="vs-mega-nav-list-item"]').find('vslink-stub');
            expect(link.text()).toBe('Attractions');
        });
    });
});
