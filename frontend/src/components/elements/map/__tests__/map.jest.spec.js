import { shallowMount } from '@vue/test-utils';

import VsMap from '../Map';
import placeData from '../../../../assets/fixtures/maps/places-data.json';

const mockAddMethod = jest.fn();
const mockMarkerMethod = jest.fn();

const factoryShallowMount = () => shallowMount(VsMap, {
    methods: {
        initialiseMapComponent: jest.fn(),
        addMapFeatures: mockAddMethod,
        addMapMarkers: mockMarkerMethod,
    },
    propsData: {
        mapId: 'vs-map',
        isVisible: true,
        places: [
        ],
        regions: [
        ],
    },
});

beforeEach(() => {
    window.URL.createObjectURL = jest.fn();
});

describe('VsMap', () => {
    it('should render a map component with the data-test attribute `vs-map`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.find('[data-test="vs-map"]').exists()).toBe(true);
    });

    describe(':props', () => {
        it('should use the `mapId` prop for an id attribute div element', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.find('.vs-map__map').attributes('id')).toBe('vs-map');
        });
    });
    describe(':methods', () => {
        it('should add map features when places props change', async() => {
            const wrapper = factoryShallowMount();

            wrapper.setProps({
                places: placeData.features,
            });
            await wrapper.vm.$nextTick();

            expect(mockAddMethod).toHaveBeenCalled();
        });

        it('should add map markers when places props change', async() => {
            const wrapper = factoryShallowMount();

            wrapper.setProps({
                places: placeData.features,
            });
            await wrapper.vm.$nextTick();

            expect(mockMarkerMethod).toHaveBeenCalled();
        });
    });
});
