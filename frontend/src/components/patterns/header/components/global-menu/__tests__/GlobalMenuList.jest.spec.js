import { shallowMount, mount } from '@vue/test-utils';

import VsGlobalMenuList from '../GlobalMenuList';

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

const factoryShallowMount = (propsData) => shallowMount(VsGlobalMenuList, {
    propsData: {
        activeSite: mainSite,
        websites: sites,
        ...propsData,
    },
});

const factoryMount = (propsData) => mount(VsGlobalMenuList, {
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

describe('<VsGlobalMenuList />', () => {
    it('should render a `<vslist-stub />`', () => {
        const dropdown = wrapper.find('vslist-stub');

        expect(dropdown.exists()).toBe(true);
    });

    it('should render 5 list items`', () => {
        const list = wrapper.findAll('li');

        expect(list.length).toBe(5);
    });

    it('should open all links that are not the `activeSite` on new tabs', () => {
        const links = wrapper.findAll('a');
        const targets = links.filter((link) => (link.attributes('href') !== mainSite));
        const blankTargets = targets.filter((link) => (link.attributes('target') === '_blank'));

        expect(blankTargets.length).toBe(4);
    });

    describe(':props', () => {
        it(':active-site - should receive the active site by props and render accordingly', () => {
            const coporateSite = 'https://www.visitscotland.org/';

            const modifiedWrapper = factoryMount({
                activeSite: coporateSite,
            });

            const link = modifiedWrapper.find('.vs-global-menu__list_item--active a');

            expect(link.exists()).toBe(true);
            expect(link.attributes('href')).toBe(coporateSite);
        });
    });
});
