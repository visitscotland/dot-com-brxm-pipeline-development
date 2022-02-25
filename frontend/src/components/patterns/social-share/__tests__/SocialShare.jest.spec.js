import { shallowMount } from '@vue/test-utils';
import VsSocialShare from '../SocialShare';

const defaultSlotText = 'Share items content';
const url = 'https://www.visitscotland.com';
const title = 'VisitScotland';

const factoryShallowMount = () => shallowMount(VsSocialShare, {
    propsData: {
        id: 'default',
        shareBtnText: 'Share',
        sharePopoverTitle: 'Share on',
        pageUrl: url,
        pageTitle: title,
        closeAltText: 'Close',
        noJs: false,
    },
    slots: {
        default: defaultSlotText,
    },
    data() {
        return {
            copyLink: false,
        };
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

    describe(':slots', () => {
        it('renders content inserted in the default slot', () => {
            expect(wrapper.text()).toContain(defaultSlotText);
        });
    });

    describe(':props', () => {
        it('should display `Share` text on the share button', () => {
            const shareButton = wrapper.find('[data-test="vs-social-share"]').find('.vs-social-share__share-btn');
            expect(shareButton.text()).toBe('Share');
        });

        it('should display `Close` text on the close button', () => {
            const closeButton = wrapper.find('[data-test="vs-social-share"]').find('.vs-social-share__close-btn');
            expect(closeButton.text()).toBe('Close');
        });

        it('should display correct `id` ', () => {
            const shareButton = wrapper.find('[data-test="vs-social-share"]').find('.vs-social-share__share-btn');
            expect(shareButton.attributes().id).toBe('vs-social-share-popover--default');
        });

        it('should display `Share on` as the title of the popover', () => {
            const heading = wrapper.find('[data-test="vs-social-share"]').find('vsheading-stub');
            expect(heading.text()).toBe('Share on');
        });

        it('should provide a `pageUrl` property down to children components', () => {
            /* eslint-disable no-underscore-dangle */
            expect(wrapper.vm._provided.pageUrl).toBe(url);
        });

        it('should provide a `pageTitle` property down to children components', () => {
            /* eslint-disable no-underscore-dangle */
            expect(wrapper.vm._provided.pageTitle).toBe(title);
        });

        it('should provide a `noJs` property down to children components', () => {
            /* eslint-disable no-underscore-dangle */
            expect(wrapper.vm._provided.noJs).toBe(false);
        });
    });
});
