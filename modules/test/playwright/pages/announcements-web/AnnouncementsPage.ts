/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Locator, Page} from '@playwright/test';

import {ApplicationsMenuPage} from '../product-navigation-applications-menu/ApplicationsMenuPage';
import {clickAndExpectToBeVisible} from '../../utils/clickAndExpectToBeVisible';

export class AnnouncementsPage {
	readonly page: Page;

	readonly applicationsMenuPage: ApplicationsMenuPage;
	readonly contentFrameLocator: FrameLocator;
	readonly contentTextBox: Locator;
	readonly newButton: Locator;
	readonly saveButton: Locator;
	readonly titleField: Locator;

	constructor(page: Page) {
		this.page = page;

		this.applicationsMenuPage = new ApplicationsMenuPage(page);
		this.contentFrameLocator = page.frameLocator('iframe');
		this.contentTextBox = this.contentFrameLocator.getByRole('textbox');
		this.newButton = page.getByRole('link', {name: 'Add Announcement'});
		this.saveButton = page.getByRole('button', {name: 'Save'});
		this.titleField = page.getByText('Title');
	}

	async goto() {
		await this.applicationsMenuPage.goToAnnouncements();
	}

	async goToCreateNewAnnouncement() {
		await this.goto();
		await this.newButton.click();
	}

	async saveNewAnnouncement(content: string, title: string) {
		await this.titleField.fill(title);
		await this.contentTextBox.fill(content);
		await this.saveButton.click();
	}

	async deleteAnnouncement(name: string) {
		await this.goto();

		await this.page.getByRole('row', {name}).getByTitle('Actions').click();
		this.page.once('dialog', (dialog) => {
			dialog.accept();
		});

		await clickAndExpectToBeVisible({
			autoClick: true,
			target: this.page.getByRole('link', {name: 'Delete'}),
			trigger: this.page.getByRole('row', {name}).getByTitle('Actions'),
		});
	}
}
