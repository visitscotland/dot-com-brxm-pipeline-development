import { shallowMount } from '@vue/test-utils';
import cookieMixin from '../../../../mixins/cookieMixin';

import VsBanner from '../Banner';

const textSlotText = 'Banner text';
const ctaSlotText = 'Banner link';

const factoryShallowMount = (propsData) => shallowMount(VsBanner, {
    propsData: {
        ...propsData,
        closeBtnText: 'close',
        title: 'Covid-19 Travel Advice',
        dontShowAgain: true,
    },
    slots: {
        bannerText: textSlotText,
        bannerCta: ctaSlotText,
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

    describe(':props', () => {
        it('should render sr-only text `close` when passed `closeBtnText` prop', () => {
            const closeBtn = wrapper.find('[data-test=vs-banner__close-btn]');
            expect(closeBtn.text()).toContain('close');
        });

        it('should render title `Covid-19 Travel Advice` when passed `title` prop', () => {
            const banner = wrapper.find('div[data-test=vs-banner');
            expect(banner.text()).toContain('Covid-19 Travel Advice');
        });

        it('does not set a cookie if `dontShowAgain` is false', async() => {
            wrapper.setProps({
                dontShowAgain: false,
            });
            const mockSetCookie = jest.fn();
            wrapper.vm.setHiddenCookie = mockSetCookie;

            await wrapper.find('[data-test=vs-banner__close-btn]').trigger('click');

            expect(mockSetCookie).not.toHaveBeenCalled();
        });
    });

    describe(':slots', () => {
        it('renders content inserted in a bannerText slot', () => {
            expect(wrapper.text()).toContain(textSlotText);
        });

        it('renders content inserted in a bannerCta slot', () => {
            expect(wrapper.text()).toContain(ctaSlotText);
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
