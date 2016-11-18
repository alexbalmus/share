/*
 * #%L
 * share-po
 * %%
 * Copyright (C) 2005 - 2016 Alfresco Software Limited
 * %%
 * This file is part of the Alfresco software. 
 * If the software was purchased under a paid Alfresco license, the terms of 
 * the paid license agreement will prevail.  Otherwise, the software is 
 * provided under the following open source license terms:
 * 
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.alfresco.po.share.site.document;

import org.alfresco.po.AbstractTest;
import org.alfresco.po.share.DashBoardPage;
import org.alfresco.po.share.RepositoryPage;
import org.alfresco.po.share.steps.SiteActions;
import org.alfresco.test.FailedTestListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.File;
import java.util.UUID;

/**
 * Tests to verify the Interactions with Links created on files / folders.
 *
 * @author adinap
 *
 */

@Listeners(FailedTestListener.class)
@Test(groups={"alfresco-one"})
public class LinkToFileFolderActionsTest extends AbstractTest
{
    private static Log logger = LogFactory.getLog(CreateLinkToFileFolderTest.class);

    private String siteName1;
    private String siteName2;
    private File file1;
    private File file2;
    private String folderName1 = "folder1";
    private String fileLinkName;
    private String folderLinkName;

    private RepositoryPage repoPage;
    private DashBoardPage dashBoard;
    private DocumentLibraryPage docLib;

    private CopyOrMoveContentPage copyOrMoveContentPage;

    @Autowired
    SiteActions siteActions;

    @BeforeClass
    public void prepare() throws Exception
    {
        try
        {
            loginAs(username, password);

            String random = UUID.randomUUID().toString();

            siteName1 = "site1-" + random;
            siteName2 = "site2-" + random;
            file1 = siteUtil.prepareFile();
            file2 = siteUtil.prepareFile();

            siteUtil.createSite(driver, username, password, siteName1, "description", "Public");
            siteUtil.createSite(driver, username, password, siteName2, "description", "Public");

            siteActions.navigateToDocumentLibrary(driver, siteName1);
            siteActions.uploadFile(driver, file1);
            siteActions.uploadFile(driver, file2);

            siteActions.createFolder(driver, folderName1, "folder title", "folder description");

//            siteActions.copyOrMoveArtifact(driver, factoryPage, CopyOrMoveContentPage.DESTINATION.ALL_SITES,
//                    siteName1, "", file1.getName(), CopyOrMoveContentPage.ACTION.CREATE_LINK);
//            fileLinkName = "Link to " + file1.getName();

//            siteActions.copyOrMoveArtifact(driver, factoryPage, CopyOrMoveContentPage.DESTINATION.ALL_SITES,
//                    siteName1, "", folderName1, CopyOrMoveContentPage.ACTION.CREATE_LINK);
//            folderLinkName = "Link to " + folderName1;
        }
        catch (Throwable pe)
        {
            saveScreenShot("CreateLinkFileUpload");
            logger.error("Cannot upload file to site ", pe);
        }
    }

    @AfterClass
    public void tearDown()
    {
        siteUtil.deleteSite(username, password, siteName1);
        siteUtil.deleteSite(username, password, siteName2);
    }

    /**
     * Check that actions available on a link to a file are correct
     */
//    @Test(priority = 0)
//    public void testLinkActionsDocLib()
//    {
//        docLib = siteActions.navigateToDocumentLibrary(driver, siteName1).render();
//
//        siteActions.copyOrMoveArtifact(driver, factoryPage, CopyOrMoveContentPage.DESTINATION.ALL_SITES,
//                siteName1, "", file1.getName(), CopyOrMoveContentPage.ACTION.CREATE_LINK);
//
//        String linkName = "Link to " + file1.getName();
//
//        Assert.assertTrue(docLib.isFileVisible(linkName));
//        
//        FileDirectoryInfo linkRow = docLib.getFileDirectoryInfo(linkName);
//
//    }

    /**
     * Check that Locate Linked Item for a link to a file redirects to 
     * document library page where the original file is located
     */
    @Test(priority = 1)
    public void testLocateLinkedItemFile()
    {
        docLib = siteActions.navigateToDocumentLibrary(driver, siteName1).render();

        siteActions.copyOrMoveArtifact(driver, factoryPage, CopyOrMoveContentPage.DESTINATION.ALL_SITES,
                siteName1, "", file1.getName(), CopyOrMoveContentPage.ACTION.CREATE_LINK);
        fileLinkName = "Link to " + file1.getName();

        FileDirectoryInfo linkRow = docLib.getFileDirectoryInfo(fileLinkName);

        docLib = linkRow.selectLocateLinkedItem().render();
        Assert.assertTrue(docLib.isFileVisible(file1.getName()));
        
        FileDirectoryInfo docRow = docLib.getFileDirectoryInfo(file1.getName());
        Assert.assertTrue(docRow.isCheckboxSelected(), "Element found, but not checked");
    }

    /**
     * Check that Locate Linked Item for a link to a folder redirects to 
     * document library page where the original folder is located
     */
    @Test(priority = 2)
    public void testLocateLinkedItemFolder()
    {
        docLib = siteActions.navigateToDocumentLibrary(driver, siteName1).render();

        siteActions.copyOrMoveArtifact(driver, factoryPage, CopyOrMoveContentPage.DESTINATION.ALL_SITES,
                siteName1, "", folderName1, CopyOrMoveContentPage.ACTION.CREATE_LINK);
        folderLinkName = "Link to " + folderName1;

        FileDirectoryInfo linkRow = docLib.getFileDirectoryInfo(folderLinkName);

        docLib = linkRow.selectLocateLinkedItem().render();
        Assert.assertTrue(docLib.isFileVisible(folderName1));

        FileDirectoryInfo docRow = docLib.getFileDirectoryInfo(folderName1);
        Assert.assertTrue(docRow.isCheckboxSelected(), "Element found, but not checked");
    }

    /**
     * Check that Delete Link action for a link deletes the link
     */
    @Test(priority = 3)
    public void testDeleteLink()
    {
        docLib = siteActions.navigateToDocumentLibrary(driver, siteName1).render();

        siteActions.copyOrMoveArtifact(driver, factoryPage, CopyOrMoveContentPage.DESTINATION.ALL_SITES,
                siteName1, "", file2.getName(), CopyOrMoveContentPage.ACTION.CREATE_LINK);

        String linkName = "Link to " + file2.getName();

        Assert.assertTrue(docLib.isFileVisible(linkName));

        FileDirectoryInfo linkRow = docLib.getFileDirectoryInfo(linkName);
        linkRow.deleteLink();

        Assert.assertFalse(docLib.isFileVisible(linkName));
    }

    /**
     * Check that clicking on a link to a file redirects to document Details page
     */
    @Test(priority = 4)
    public void testClickLinkToFile()
    {
        docLib = siteActions.navigateToDocumentLibrary(driver, siteName1).render();

        siteActions.copyOrMoveArtifact(driver, factoryPage, CopyOrMoveContentPage.DESTINATION.ALL_SITES,
                siteName1, "", file1.getName(), CopyOrMoveContentPage.ACTION.CREATE_LINK);
        fileLinkName = "Link to " + file1.getName();
        
        FileDirectoryInfo linkRow = docLib.getFileDirectoryInfo(fileLinkName);

        DocumentDetailsPage documentDetailsPage = linkRow.clickOnTitle().render();
        
        Assert.assertTrue(documentDetailsPage.getDocumentTitle().equalsIgnoreCase(file1.getName()));
    }

    /**
     * Check that clicking on a link to a folder redirects to folder contents page
     */
    //enable this after SHA-1864 is fixed
    @Test(priority = 5, enabled = false)
    public void testClickLinkToFolder()
    {
        docLib = siteActions.navigateToDocumentLibrary(driver, siteName1).render();

        siteActions.copyOrMoveArtifact(driver, factoryPage, CopyOrMoveContentPage.DESTINATION.ALL_SITES,
                siteName1, "", folderName1, CopyOrMoveContentPage.ACTION.CREATE_LINK);
        folderLinkName = "Link to " + folderName1;

        FileDirectoryInfo linkRow = docLib.getFileDirectoryInfo(folderLinkName);
        docLib = linkRow.clickOnTitle().render();
        
        String path = docLib.getNavigation().getCrumbsElementDetailsLinkName();
        
        Assert.assertTrue(path.equalsIgnoreCase(folderName1));
    }
    
}
