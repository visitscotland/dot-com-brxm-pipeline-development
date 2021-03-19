import { shallowMount } from '@vue/test-utils';

import VsButtonSquareSocial from '../ButtonSquareSocial';

const factoryShallowMount = (propsData) => shallowMount(VsButtonSquareSocial, {
    propsData: {
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount({
        icon: 'facebook',
        href: '#facebook.com',
    });
});

describe('VsButtonSquareSocial', () => {
    it('should render ButtonSquareSocial', () => {
        const ButtonSquareSocial = wrapper.find('vslink-stub.button-square-social');

        expect(ButtonSquareSocial.exists()).toBe(true);
    });

    describe(':props', () => {
        it(':icon - Social icon to show', () => {
            const vsLink = wrapper.find('vsicon-stub');

            expect(vsLink.attributes('name')).toBe('facebook');
        });

        it(':href - Social the external URL the button will point to', () => {
            const vsLink = wrapper.find('vslink-stub.button-square-social');

            expect(vsLink.attributes('href')).toBe('#facebook.com');
        });
    });
});
