import { shallowMount } from '@vue/test-utils';
import cookieMixin from '../../../../mixins/cookieMixin';

import VsBanner from '../Banner';

const titleSlotText = 'Banner title';
const textSlotText = 'Banner text';
const ctaSlotText = 'Banner link';
const closeBtnSlotText = 'Close';

const factoryShallowMount = () => shallowMount(VsBanner, {
    slots: {
        bannerTitle: titleSlotText,
        bannerText: textSlotText,
        bannerCta: ctaSlotText,
        closeBtnText: closeBtnSlotText,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsBanner', () => {
    it('should render a component with the data-test attribute `vs-banner`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-banner');
    });

    describe(':slots', () => {
        it('renders content inserted in a bannerTitle slot', () => {
            expect(wrapper.text()).toContain(titleSlotText);
        });

        it('renders content inserted in a bannerText slot', () => {
            expect(wrapper.text()).toContain(textSlotText);
        });

        it('renders content inserted in a bannerCta slot', () => {
            expect(wrapper.text()).toContain(ctaSlotText);
        });

        it('renders content inserted in a closeBtnText slot', () => {
            expect(wrapper.text()).toContain(closeBtnSlotText);
        });
    });

    describe(':methods', () => {
        it('hides banner on close button click', async() => {
            await wrapper.find('[data-test=vs-banner__close-btn]').trigger('click');
            const banner = wrapper.find('[data-test=vs-banner]');

            expect(banner.exists()).toBe(false);
        });

        it('should set the cookie when close button is clicked', async() => {
            const mockSet = jest.fn();
            cookieMixin.setCookie = mockSet;
            cookieMixin.setCookie('vs_showbanner', false, false, true);

            expect(mockSet).toBeCalled();
        });

        it('should hide banner if cookie is already set when mounted', async() => {
            const mockGet = jest.fn();
            cookieMixin.cookieExists = mockGet;
            cookieMixin.cookieExists('vs_showbanner');
            const banner = wrapper.find('[data-test=vs-banner]');

            expect(mockGet).toBeCalled();
            expect(banner.exists()).toBe(false);
        });
    });
});
