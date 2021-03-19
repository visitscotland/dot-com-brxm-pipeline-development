import { shallowMount } from '@vue/test-utils';

import VsSocialShare from '../SocialShare';

const factoryShallowMount = (propsData) => shallowMount(VsSocialShare, {
    propsData: {
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsSocialShare', () => {
    it('should render a component with the data-test attribute `vs-social-share`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-social-share');
    });
});
