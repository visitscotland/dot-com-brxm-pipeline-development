import { shallowMount } from '@vue/test-utils';
import VsEmbedWrapper from '../EmbedWrapper';

const introContent = 'Intro text';
const noJsContent = 'Js is off';
const noCookiesContent = 'Cookies are off';
const widgetcontent = 'A script tag';
const errorContent = 'Error content';

const factoryShallowMount = () => shallowMount(VsEmbedWrapper, {
    slots: {
        embedIntroCopy: introContent,
        embedIntroCopyNoJs: noJsContent,
        embedIntroCopyNoCookies: noCookiesContent,
        embedIntroCopyError: errorContent,
        embedWidget: widgetcontent,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsEmbedWrapper', () => {
    it('should render a component with the data-test attribute `.vs-embed-wrapper`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-embed-wrapper');
    });

    describe(':slots', () => {
        it('renders content inserted into the `embedIntroCopy` slot', async() => {
            wrapper.setData({
                cookiesInitStatus: true,
            });
            await wrapper.vm.$nextTick();

            expect(wrapper.text()).toContain(introContent);
        });

        it('renders content inserted into the `embedIntroCopyNoJs` slot', () => {
            expect(wrapper.text()).toContain(noJsContent);
        });

        it('renders content inserted into the `embedIntroCopyNoCookies` slot', async() => {
            wrapper.setData({
                cookiesInitStatus: true,
            });
            await wrapper.vm.$nextTick();

            expect(wrapper.text()).toContain(noCookiesContent);
        });

        it('renders content inserted into the `embedWidget` slot', () => {
            expect(wrapper.text()).toContain(widgetcontent);
        });
    });
});
