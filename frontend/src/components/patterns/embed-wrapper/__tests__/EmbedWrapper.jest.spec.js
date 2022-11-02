import { shallowMount, mount } from '@vue/test-utils';
import VsEmbedWrapper from '../EmbedWrapper';

const introContent = 'Intro text';
const noJsContent = 'Js is off';
const noCookiesContent = 'Cookies are off';
const widgetcontent = 'A script tag';
const errorContent = 'Error content';

const factoryShallowMount = (computed) => shallowMount(VsEmbedWrapper, {
    slots: {
        embedIntroCopy: introContent,
        embedWidget: widgetcontent,
    },
    propsData: {
        noCookieText: noCookiesContent,
        errorText: errorContent,
        noJsText: noJsContent,
    },
    computed: {
        ...computed,
    },
});

const factoryMount = (computed) => mount(VsEmbedWrapper, {
    slots: {
        embedIntroCopy: introContent,
        embedWidget: widgetcontent,
    },
    propsData: {
        noCookieText: noCookiesContent,
        errorText: errorContent,
        noJsText: noJsContent,
    },
    computed: {
        ...computed,
    },
});

describe('VsEmbedWrapper', () => {
    it('should render a component with the data-test attribute `.vs-embed-wrapper`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.attributes('data-test')).toBe('vs-embed-wrapper');
    });

    describe(':slots', () => {
        it('renders content inserted into the `embedIntroCopy` slot', async() => {
            const wrapper = factoryShallowMount();
            wrapper.setData({
                cookiesInitStatus: true,
            });
            await wrapper.vm.$nextTick();

            expect(wrapper.text()).toContain(introContent);
        });

        it('renders content inserted into the `embedIntroCopyNoJs` slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.text()).toContain(noJsContent);
        });

        it('renders content inserted into the `embedIntroCopyNoCookies` slot', async() => {
            const wrapper = factoryMount({
                requiredCookiesExist: {
                    get() {
                        return false;
                    },
                },
                showError: {
                    get() {
                        return true;
                    },
                },
            });
            wrapper.setData({
                cookiesInitStatus: true,
            });

            await wrapper.vm.$nextTick();

            expect(wrapper.text()).toContain(noCookiesContent);
        });

        it('renders content inserted into the `embedWidget` slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.text()).toContain(widgetcontent);
        });
    });
});
