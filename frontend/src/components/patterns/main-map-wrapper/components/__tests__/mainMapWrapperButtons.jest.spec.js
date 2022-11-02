import { shallowMount } from '@vue/test-utils';
import VsMainMapWrapperButtons from '../MainMapWrapperButtons';

const factoryShallowMount = () => shallowMount(VsMainMapWrapperButtons, {
    propsData: {
        contentData: {
            properties: {
                category: {
                    id: 'cities',
                    label: 'Cities',
                },
                title: 'Aberdeen',
                id: 'aberdeen',
                description: 'Aberdeen description',
                image: 'https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm',
                link: {
                    label: 'Discover Aberdeen',
                    link: 'http://172.28.81.65:8089/info/see-do/national-museum-of-scotland-p246591',
                    type: 'INTERNAL',
                },
            },
        },
    },
});

describe('VsMainMapWrapperButtons', () => {
    it('should render an element with the `vs-main-map-wrapper-buttons` data-test attribute', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-main-map-wrapper-buttons');
    });

    describe(':props', () => {
        it('should render the title from the `contentData` prop', () => {
            const wrapper = factoryShallowMount();

            const discoverBtn = wrapper.find('[data-test="vs-main-map-wrapper-buttons__discover"]');

            expect(discoverBtn.text()).toBe('Discover Aberdeen');
        });
    });
});
