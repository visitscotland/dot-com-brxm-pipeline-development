import { shallowMount } from '@vue/test-utils';
import VsSvgLink from '../SvgLink';

const factoryShallowMount = (values) => shallowMount(VsSvgLink, {
    propsData: {
        linkAltText: 'VisitScotland Home',
        href: 'https://www.visitscotland.com/',
        svgFill: '700e57',
        svgPath: 'visitscotland',
        ...values,
    },
});

describe('VsSvgLink', () => {
    it('should render a component with the data-test attribute `.vs-svg-link`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.attributes('data-test')).toBe('vs-svg-link');
    });

    describe(':props', () => {
        it('should render alt text `VisitScotland Home` when passed as `linkAltText` prop', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="link-alt-text"]').text()).toBe('VisitScotland Home');
        });

        it('should render url `https://www.visitscotland.com/` when passed as `href` prop', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-svg-link"]').attributes().href).toBe('https://www.visitscotland.com/');
        });

        it('should render colour `700e57` when passed as `svgFill` prop', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-svg-link__svg"]').attributes().fill).toBe('700e57');
        });

        it('should render path `svgPath` when passed as `svgPath` prop', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-svg-link__svg"]').attributes().path).toBe('visitscotland');
        });

        it('should render variant `on-dark` when passed as `linkVariant` prop', () => {
            const wrapper = factoryShallowMount({
                variant: 'on-dark',
            });
            expect(wrapper.find('[data-test="vs-svg-link"]').attributes().variant).toBe('on-dark');
        });
    });
});
