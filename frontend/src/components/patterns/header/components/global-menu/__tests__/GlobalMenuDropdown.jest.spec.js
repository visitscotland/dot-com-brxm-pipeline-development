import { shallowMount, mount } from '@vue/test-utils';

import VsGlobalMenuDropdown from '../GlobalMenuDropdown';

const mainSite = 'https://www.visitscotland.com/';

const sites = [
    {
        siteName: 'VisitScotland',
        siteUrl: 'https://www.visitscotland.com/',
    },
    {
        siteName: 'Business Events',
        siteUrl: 'https://businessevents.visitscotland.com/',
    },
    {
        siteName: 'Travel Trade',
        siteUrl: 'https://traveltrade.visitscotland.org/',
    },
    {
        siteName: 'Media Centre',
        siteUrl: 'https://media.visitscotland.org/',
    },
    {
        siteName: 'Corporate',
        siteUrl: 'https://www.visitscotland.org/',
    },
];

const factoryShallowMount = (propsData) => shallowMount(VsGlobalMenuDropdown, {
    propsData: {
        activeSite: mainSite,
        websites: sites,
        ...propsData,
    },
});

const factoryMount = (propsData) => mount(VsGlobalMenuDropdown, {
    propsData: {
        activeSite: mainSite,
        websites: sites,
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('<VsGlobalMenuDropdown />', () => {
    it('should render a `<vsdropdown-stub />`', () => {
        const dropdown = wrapper.find('vsdropdown-stub');

        expect(dropdown.exists()).toBe(true);
    });

    it('should render 5 dropdown items`', () => {
        const list = wrapper.findAll('vsdropdownitem-stub');

        expect(list.length).toBe(5);
    });

    it('should not display the active website`', () => {
        const list = wrapper.findAll('vsdropdownitem-stub');
        const active = list.filter((link) => link.attributes('href') === mainSite);

        expect(active.length).toBe(1);
        expect(active.at(0).classes()).toContain('d-none');
    });

    it('should open all links that are not the `activeSite` on new tabs', () => {
        const links = wrapper.findAll('vsdropdownitem-stub');
        const targets = links.filter((link) => (link.attributes('href') !== mainSite));
        const blankTargets = targets.filter((link) => (link.attributes('target') === '_blank'));

        expect(blankTargets.length).toBe(4);
    });

    describe(':props', () => {
        it(':ourWebsitesLabel - should render "Our Websites" as the default translation label', () => {
            const modifiedWrapper = factoryMount();

            const dropdown = modifiedWrapper.find('.vs-dropdown');

            expect(dropdown.text()).toContain('Our Websites');
        });

        it(':ourWebsitesLabel - should receive the translation label by props and render accordingly', () => {
            const translationLabel = 'i nostri siti';

            const modifiedWrapper = factoryMount({
                dropdownLabel: translationLabel,
            });

            const dropdown = modifiedWrapper.find('.vs-dropdown');

            expect(dropdown.text()).toContain(translationLabel);
        });
    });
});
