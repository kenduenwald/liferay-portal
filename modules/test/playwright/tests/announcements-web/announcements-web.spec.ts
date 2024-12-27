/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect, mergeTests} from '@playwright/test';

import {announcementsPagesTest} from '../../fixtures/announcementsPagesTest';
import {loginTest} from '../../fixtures/loginTest';

export const test = mergeTests(announcementsPagesTest, loginTest());

test('LPD-18804: Do not have a blank option for distribution scope', async ({
	announcementsPage,
	page,
}) => {
	await announcementsPage.goToCreateNewAnnouncement();

	await page.getByRole('button', {name: 'Configuration'}).click();
	await page.getByLabel('Distribution Scope').selectOption({index: 0});

	expect(
		await page
			.getByLabel('Distribution Scope')
			.evaluate(
				(select: HTMLSelectElement) =>
					select.options[select.selectedIndex].label
			)
	).toBe('General');
});

test('LPD-27067 Content field is required', async ({
	announcementsPage,
	page,
}) => {
	await announcementsPage.goToCreateNewAnnouncement();

	await expect(page.getByText('Content *')).toBeVisible();
});


test('LPD-28065 Can Create Announcement', async ({
	announcementsPage,
	page,
}) => {
	const content = 'Announcements Entry Content';
	const title = 'Announcements Entry Title';
	const name = 'Announcements Entry Title';

	await announcementsPage.goToCreateNewAnnouncement();

	await announcementsPage.saveNewAnnouncement(
		content,
		title
	);

	await expect(
		page.getByText('Success:Your request completed successfully.')
	).toBeVisible();

	await page.getByText(title).click();

	await expect(
		page.getByText(title)
	).toBeVisible();

	await expect(
		page.getByText(content)
	).toBeVisible();

	// Clean up

	await announcementsPage.deleteAnnouncement(
		name
	);	
});