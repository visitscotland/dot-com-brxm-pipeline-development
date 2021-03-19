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
            </VsMegaNavTopMenuItem>
        </template>

        <template #megaNavMobileItems>
            <VsAccordion>
                <VsMegaNavAccordionItem
                    :title="item.title"
                    level="1"
                    :control-id="mobileItemIndex"
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
                        :control-id="subHeadingIndex"
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
                </VsMegaNavAccordionItem>
            </VsAccordion>
        </template>
    </VsMegaNav>
</template>
