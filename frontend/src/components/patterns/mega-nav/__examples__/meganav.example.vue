<template>
    <VsMegaNav
        href="/"
        menu-toggle-alt-text="Toggle Menu"
    >
        <template #megaNavTopMenuItems>
            <VsMegaNavTopMenuItem
                v-for="(item, index) in header.mainNav"
                :key="index"
                :href="item.href"
                :cta-text="item.cta"
                :align="item.title === 'Accommodation'
                    ? 'bottom' : 'top'"
            >
                <template #buttonContent>
                    {{ item.title }}
                </template>
                <template #dropdownContent>
                    <VsMegaNavList
                        v-for="(subHeading, subHeadingIndex) in item.dropdownNav"
                        :key="subHeadingIndex"
                    >
                        <VsMegaNavListItem slot="navListHeading">
                            {{ subHeading.title }}
                        </VsMegaNavListItem>

                        <VsMegaNavListItem
                            slot="navListItems"
                            v-for="(navLink, navLinkIndex)
                                in subHeading.dropdownNav"
                            :key="navLinkIndex"
                            :href="navLink.href"
                        >
                            {{ navLink.title }}
                        </VsMegaNavListItem>

                        <VsMegaNavListItem
                            v-if="subHeading.href"
                            :href="subHeading.href"
                            subheading-link
                            slot="navHeadingCtaLink"
                        >
                            {{ subHeading.cta }}
                        </VsMegaNavListItem>
                    </VsMegaNavList>
                </template>

                <template
                    slot="navFeaturedItem"
                    v-if="item.title === 'Accommodation' || item.title === 'Inspiration'"
                >
                    <VsMegaNavFeaturedItem
                        link="www.visitscotland.com"
                        img-url="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                        img-alt="Alt text"
                    >
                        <template slot="vsFeaturedItemHeader">
                            From our home to yours – see Scotland virtually
                        </template>

                        <template slot="vsFeaturedItemContent">
                            <p>
                                Although it’s not possible to come to Scotland at the moment,
                                thanks to modern technology, you can still see stunning
                            </p>
                        </template>

                        <template slot="vsFeaturedItemLink">
                            A link to a page
                        </template>
                    </VsMegaNavFeaturedItem>
                </template>

                <div class="featured-items">
                    <template
                        slot="navFeaturedItemLeft"
                        v-if="item.title === 'Inspiration'"
                    >
                        <VsMegaNavFeaturedItem
                            link="www.visitscotland.com"
                            img-url="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                            img-alt="Alt text"
                            v-if="item.title==='Inspiration'"
                        >
                            <template slot="vsFeaturedItemHeader">
                                Featured item left
                            </template>

                            <template slot="vsFeaturedItemContent">
                                <p>
                                    Although it’s not possible to come to
                                    Scotland at the moment, thanks to modern
                                    technology, you can still see stunning
                                </p>
                            </template>

                            <template slot="vsFeaturedItemLink">
                                A link to a page
                            </template>
                        </VsMegaNavFeaturedItem>
                    </template>
                </div>
            </VsMegaNavTopMenuItem>
        </template>

        <template #megaNavAccordionItems>
            <VsAccordion>
                <VsMegaNavAccordionItem
                    :title="item.title"
                    level="1"
                    :control-id="mobileItemIndex.toString()"
                    v-for="(item, mobileItemIndex) in header.mainNav"
                    :key="mobileItemIndex"
                >
                    <VsMegaNavList v-if="item.href && item.cta">
                        <VsMegaNavListItem
                            slot="navListHeading"
                            :href="item.href"
                            cta-link
                        >
                            {{ item.cta }}
                        </VsMegaNavListItem>
                    </VsMegaNavList>

                    <VsMegaNavAccordionItem
                        :title="subHeading.title"
                        level="2"
                        :control-id="subHeadingIndex.toString()"
                        v-for="(subHeading, subHeadingIndex) in item.dropdownNav"
                        :key="subHeadingIndex"
                    >
                        <VsMegaNavList>
                            <VsMegaNavListItem
                                slot="navListItems"
                                v-for="(navLink, navLinkIndex)
                                    in subHeading.dropdownNav"
                                :key="navLinkIndex"
                                :href="navLink.href"
                            >
                                {{ navLink.title }}
                            </VsMegaNavListItem>

                            <VsMegaNavListItem
                                v-if="subHeading.href"
                                :href="subHeading.href"
                                subheading-link
                                slot="navHeadingCtaLink"
                            >
                                {{ subHeading.cta }}
                            </VsMegaNavListItem>
                        </VsMegaNavList>
                    </VsMegaNavAccordionItem>
                    <div class="featured-items">
                        <template
                            v-if="item.title === 'Accommodation' || item.title === 'Inspiration'"
                        >
                            <VsMegaNavFeaturedItem
                                link="www.visitscotland.com"
                                img-url="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                                img-alt="Alt text"
                            >
                                <template slot="vsFeaturedItemHeader">
                                    From our home to yours – see Scotland virtually
                                </template>

                                <template slot="vsFeaturedItemContent">
                                    <p>
                                        Although it’s not possible to come to
                                        Scotland at the moment.
                                    </p>
                                </template>

                                <template slot="vsFeaturedItemLink">
                                    A link to a page
                                </template>
                            </VsMegaNavFeaturedItem>
                        </template>
                        <template
                            v-if="item.title === 'Inspiration'"
                        >
                            <VsMegaNavFeaturedItem
                                link="www.visitscotland.com"
                                img-url="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                                img-alt="Alt text"
                                v-if="item.title==='Inspiration'"
                            >
                                <template slot="vsFeaturedItemHeader">
                                    Featured item left
                                </template>

                                <template slot="vsFeaturedItemContent">
                                    <p>
                                        Although it’s not possible to come to Scotland
                                        at the moment, thanks to modern technology, you
                                        can still see stunning
                                    </p>
                                </template>

                                <template slot="vsFeaturedItemLink">
                                    A link to a page
                                </template>
                            </VsMegaNavFeaturedItem>
                        </template>
                    </div>
                </VsMegaNavAccordionItem>
            </VsAccordion>
        </template>
    </VsMegaNav>
</template>
