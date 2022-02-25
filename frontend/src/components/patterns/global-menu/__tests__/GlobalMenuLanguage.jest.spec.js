import { shallowMount, mount } from '@vue/test-utils';
import VsGlobalMenuLanguage from '../components/GlobalMenuLanguage';

const factoryShallowMount = (propsData) => shallowMount(VsGlobalMenuLanguage, {
    slots: {
        default: 'Button text',
    },
    propsData: {
        languageLabel: 'Language',
        ...propsData,
    },
});

describe('VsGlobalMenuLanguage', () => {
    it('should render a dropdown', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.element.tagName).toBe('VSDROPDOWN-STUB');
    });
});

describe(':props', () => {
    it('should render correct language label on dropdown button', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.find('.vs-global-menu__languages__label').text()).toEqual('Language');
    });

    it('should render selected language', () => {
        const wrapper = mount(VsGlobalMenuLanguage, {
            propsData: {
                language: 'IT',
            },
        });

        expect(wrapper.find('.vs-global-menu__languages__selected').text()).toEqual('IT');
    });
});
