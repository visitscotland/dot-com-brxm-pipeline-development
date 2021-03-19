import { shallowMount } from '@vue/test-utils';

import VsImageLocationMap from '../ImageLocationMap';

const slotContent = 'Slot Content';

const factoryShallowMount = (propsData) => shallowMount(VsImageLocationMap, {
    propsData: {
        ...propsData,
        latitude: '57',
        longitude: '-2.2',
        mapOutlineColor: '#FF0000',
        mapMarkerColor: '#00FF00',
    },
    slots: {
        default: slotContent,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsImageLocationMap', () => {
    it('should render a component with the data-test attribute `vs-image-location-map`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-image-location-map');
    });

    describe(':props', () => {
        it('should accept and render a `mapOutlineColor` property', () => {
            const path = wrapper.find('[data-test="vs-image-location-map__first-path"]');
            expect(path.attributes('stroke')).toBe('#FF0000');
        });

        it('should accept and render a `mapMarkerColor` property', () => {
            const marker = wrapper.find('[data-test="vs-image-location-map__marker"]');
            expect(marker.attributes('fill')).toBe('#00FF00');
        });

        it('should accept and render a `longitude` property', () => {
            const marker = wrapper.find('[data-test="vs-image-location-map__marker"]');
            // 54 is the expected x coord on the svg for a lattitude of 57
            expect(marker.attributes('cx')).toBe('54');
        });

        it('should accept and render a `latitude` property', () => {
            const marker = wrapper.find('[data-test="vs-image-location-map__marker"]');
            // 65 is the expected y coord on the svg for a latitude of -2.2
            expect(marker.attributes('cy')).toBe('65');
        });
    });
});
