import { shallowMount } from '@vue/test-utils';
import VsMainMapWrapperToggle from '../MainMapWrapperToggle';

const factoryShallowMount = () => shallowMount(VsMainMapWrapperToggle);

describe('VsMainMapWrapperToggle', () => {
    it('should render an element with the `vs-main-map-wrapper-toggle` data-test attribute', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-main-map-wrapper-toggle');
    });
});
