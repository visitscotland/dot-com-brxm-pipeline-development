import { shallowMount } from '@vue/test-utils';
import VsMainMapWrapperDetail from '../MainMapWrapperDetail';

const factoryShallowMount = () => shallowMount(VsMainMapWrapperDetail, {
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
                link: 'https://www.visitscotland.com/',
            },
        },
    },
});

describe('VsMainMapWrapperCategory', () => {
    it('should render an element with the `vs-main-map-wrapper-detail` data-test attribute', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-main-map-wrapper-detail');
    });

    describe(':props', () => {
        it('should render a title with the value defined in the `ContentData` prop', () => {
            const wrapper = factoryShallowMount();
            const title = wrapper.find('[data-test="vs-main-map-wrapper-detail__heading"]');

            expect(title.text()).toBe('Aberdeen');
        });

        it('should render an image with the src attribute defined in the `ContentData` prop', () => {
            const wrapper = factoryShallowMount();
            const img = wrapper.find('vsimg-stub');

            expect(img.attributes('src')).toBe('https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm');
        });

        it('should render a description with the value defined in the `ContentData` prop', () => {
            const wrapper = factoryShallowMount();
            const description = wrapper.find('[data-test="vs-main-map-wrapper-detail__description"]');

            expect(description.text()).toBe('Aberdeen description');
        });
    });
});
