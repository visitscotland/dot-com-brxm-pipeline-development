import { shallowMount } from '@vue/test-utils';
import VsTagManagerWrapper from '../TagManagerWrapper';

const factoryShallowMount = () => shallowMount(VsTagManagerWrapper);

describe('VsTagManagerWrapper', () => {
    it('should render a b-link with class vs-link', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.find('span[data-test=vs-tag-manager-wrapper]').exists()).toBe(true);
    });
});
