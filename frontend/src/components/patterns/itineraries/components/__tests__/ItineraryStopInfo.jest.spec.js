import { shallowMount } from '@vue/test-utils';
import VsItineraryStopInfo from '../ItineraryStopInfo';
import openingTimesData from './data/openingTimesData.json';
import openingTimesDataClosed from './data/openingTimesDataClosed.json';

const currentDayData = [{
    key: 'thursday',
    day: 'Thursdays',
    startTime: '09:30',
    endTime: '18:00',
    state: 'open',
    provisional: true,
}];
const currentDayDataUsual = [{
    key: 'thursday',
    day: 'Thursdays',
    startTime: '09:30',
    endTime: '18:00',
    state: 'open',
    provisional: false,
}];
const dayDataMultiple = [
    {
        key: 'thursday',
        day: 'Thursdays',
        startTime: '09:30',
        endTime: '12:00',
        state: 'open',
        provisional: false,
    },
    {
        key: 'thursday',
        day: 'Thursdays',
        startTime: '12:30',
        endTime: '18:00',
        state: 'open',
        provisional: false,
    },
];
const dayDataNoTimes = [{
    key: 'thursday',
    day: 'Thursdays',
    startTime: '',
    endTime: '',
    provisional: false,
}];

const dayDataClosedState = [{
    key: 'thursday',
    day: 'Thursdays',
    startTime: '',
    endTime: '',
    state: 'closed',
    provisional: false,
}];

const factoryShallowMount = () => shallowMount(VsItineraryStopInfo, {
    propsData: {
        openingTime: '09:00',
        closingTime: '17:00',
        openingTimesLink: 'http://www.visitscotland.com',
        openingHours: openingTimesData.openingHours,
        closedText: 'Closed',
        closingSoonText: 'Closing soon',
        openText: 'Open',
        usualText: 'Usually',
        provisionalText: 'Provisionally',
        temporarilyClosedText: 'Temporarily closed',
        toText: 'To text',
        andText: 'And text',
    },
    slots: {
        'stop-link-text': 'All opening times',
        'stop-charge-text': 'Free admission',
    },
});

let wrapper;
beforeEach(async() => {
    wrapper = factoryShallowMount();

    await wrapper.setData({
        currentDayData,
        ukDate: '25/11/2021',
        dayDataIndex: 0,
        openingMessage: 'open',
        isCurrentTimeframe: false,
        parsedHours: openingTimesData.openingHours,
    });

    await wrapper.vm.isActiveDate();
});

describe('VsItineraryStopInfo', () => {
    it('should render a component with the data-test attribute `vs-itinerary-stop-info`', async() => {
        expect(wrapper.find('[data-test="vs-itinerary-stop-info"]').exists()).toBe(true);
    });

    describe(':props', () => {
        it('renders content inserted into the slot when the stop is open', () => {
            expect(wrapper.find('[data-test="vs-itinerary-stop-status"]').text()).toContain('Open');
        });

        it('renders content inserted into the slot when the stop is closed', async() => {
            await wrapper.setData({
                openingMessage: 'closed',
            });

            expect(wrapper.find('[data-test="vs-itinerary-stop-status"]').text()).toContain('Closed');
        });

        it('renders content inserted into the slot when the stop is closing soon', async() => {
            await wrapper.setData({
                openingMessage: 'closing soon',
            });
            expect(wrapper.find('[data-test="vs-itinerary-stop-status"]').text()).toContain('Closing soon');
        });

        it('renders the correct provisional text', async() => {
            await wrapper.setData({
                openingMessage: 'closed',
            });

            expect(wrapper.find('[data-test="vs-itinerary-stop-hours"]').text()).toContain('Provisionally');
        });

        it('renders the correct usual text', async() => {
            await wrapper.setData({
                currentDayData: currentDayDataUsual,
            });

            expect(wrapper.find('[data-test="vs-itinerary-stop-hours"]').text()).toContain('Usually');
        });

        it('renders the correct text if it is long term closed', async() => {
            wrapper.setData({
                closedLongTerm: true,
                openingMessage: 'closed',
            });

            await wrapper.vm.$nextTick();

            expect(wrapper.find('[data-test="vs-itinerary-stop-status"]').text()).toContain('Temporarily closed');
        });

        it('renders the `to` text prop correctly', async() => {
            expect(wrapper.find('[data-test="vs-itinerary-stop-hours"]').text()).toContain('To text');
        });

        it('renders the `and` text prop correctly', async() => {
            await wrapper.setData({
                currentDayData: dayDataMultiple,
            });

            expect(wrapper.find('[data-test="vs-itinerary-stop-hours"]').text()).toContain('And text');
        });

        it('only shows the opening hours link if a prop is supplied', async() => {
            await wrapper.setProps({
                openingTimesLink: null,
            });

            expect(wrapper.find('[data-test="vs-stop-link"]').exists()).toBe(false);
        });
    });

    describe(':computed', () => {
        it('only does not render an opening status messages if hoursMessage is not defined', async() => {
            await wrapper.setData({
                currentDayData: dayDataNoTimes,
                openingMessage: '',
            });

            const currentTime = {
                day: 4,
                hours: 12,
                minutes: 30,
            };

            await wrapper.vm.compareTimes(currentTime, dayDataNoTimes, 0);

            expect(wrapper.find('[data-test="vs-itinerary-stop-status"]').exists()).toBe(false);
        });
    });

    describe(':slots', () => {
        it('renders content inserted into the vs-stop-link-text slot', async() => {
            await wrapper.setData({
                openingMessage: 'closing soon',
            });
            await wrapper.vm.$nextTick();
            expect(wrapper.find('[data-test="vs-stop-link"]').html()).toContain('All opening times');
        });

        it('renders content inserted into the vs-stop-charge-text slot', async() => {
            expect(wrapper.find('[data-test="vs-stop-charge-text"]').text()).toBe('Free admission');
        });
    });

    describe(':methods', () => {
        it('correctly defines whether or not todays date is within the active date parameters', async() => {
            expect(wrapper.vm.isCurrentTimeframe).toBe(true);
        });

        it('sets the openingMessage data to `closed` if the current time is outside of the opening hours', async() => {
            await wrapper.setData({
                currentTime: {
                    day: 4,
                    hours: 18,
                    minutes: 30,
                },
            });

            wrapper.vm.getCurrentHoursInfo();

            expect(wrapper.vm.openingMessage).toBe('closed');
        });

        it('sets the openingMessage data to `open` if the current time is inside the opening hours', async() => {
            await wrapper.setData({
                currentTime: {
                    day: 4,
                    hours: 12,
                    minutes: 30,
                },
            });

            wrapper.vm.getCurrentHoursInfo();

            expect(wrapper.vm.openingMessage).toBe('open');
        });

        it('sets the openingMessage data to `closing` if the current time is within 30 mins of the closing time', async() => {
            await wrapper.setData({
                currentTime: {
                    day: 4,
                    hours: 17,
                    minutes: 40,
                },
            });

            wrapper.vm.getCurrentHoursInfo();

            expect(wrapper.vm.openingMessage).toBe('closing soon');
        });

        it('shows closed message if the state is set to `closed`', async() => {
            const currentTime = {
                day: 4,
                hours: 12,
                minutes: 0,
            };

            await wrapper.vm.compareTimes(currentTime, dayDataClosedState, 0);

            expect(wrapper.find('[data-test="vs-itinerary-stop-status"]').html()).toContain('Closed');
        });

        it('sets the openingMessage data to blank if no times are in the data', async() => {
            await wrapper.setData({
                openingMessage: '',
            });

            const currentTime = {
                day: 4,
                hours: 17,
                minutes: 40,
            };

            wrapper.vm.compareTimes(currentTime, dayDataNoTimes, 0);

            expect(wrapper.vm.openingMessage).toBe('');
            expect(wrapper.find('[data="vs-itinerary-stop-status"]').exists()).toBe(false);
            expect(wrapper.find('[data="vs-itinerary-stop-hours"]').exists()).toBe(false);
        });

        it('sets currentTimeframe to false if current date falls outside of the dates in the opening hours data', async() => {
            await wrapper.setProps({
                openingHours: openingTimesDataClosed.openingHours,
            });
            await wrapper.setData({
                parsedHours: openingTimesDataClosed.openingHours,
            });

            await wrapper.vm.isActiveDate();
            expect(wrapper.vm.isCurrentTimeframe).toBe(false);
            expect(wrapper.find('[data="vs-itinerary-stop-status"]').exists()).toBe(false);
            expect(wrapper.find('[data="vs-itinerary-stop-hours"]').exists()).toBe(false);
        });
    });
});
