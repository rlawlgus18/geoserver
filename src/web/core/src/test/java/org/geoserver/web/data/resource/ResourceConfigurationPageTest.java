/* (c) 2014 - 2016 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.resource;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.data.test.MockData;
import org.geoserver.web.GeoServerWicketTestSupport;
import org.junit.Test;

public class ResourceConfigurationPageTest extends GeoServerWicketTestSupport {
    
    @Test
    public void testBasic() {
        LayerInfo layer = getGeoServerApplication().getCatalog().getLayers().get(0);

        login();
        tester.startPage(new ResourceConfigurationPage(layer, false));
        tester.assertLabel("publishedinfoname", layer.getResource().getPrefixedName());
        tester.assertComponent("publishedinfo:tabs:panel:theList:0:content", BasicResourceConfig.class);
    }
    
    @Test
    public void testUpdateResource() {
        LayerInfo layer = getGeoServerApplication().getCatalog().getLayers().get(0);

        login();
        ResourceConfigurationPage page = new ResourceConfigurationPage(layer, false);
        
        tester.startPage(page);
        tester.assertContainsNot("the_geom");
        
        FeatureTypeInfo info = getCatalog().getResourceByName(MockData.BRIDGES.getLocalPart(), FeatureTypeInfo.class);

        //Apply the new feature to the page
        page.add(new AjaxEventBehavior("ondblclick") {
            public void onEvent(AjaxRequestTarget target) {
                page.updateResource(info, target);
            }
        });
        tester.executeAjaxEvent(page, "ondblclick");
        
        //verify contents were updated
        tester.assertContains("the_geom");
    }
    
    // I can't make the last assertion work, my wicket-fu is not good enough or else the
    // 
//    public void testTabSwitch() {
//        ResourceInfo info = getGeoServerApplication()
//            .getCatalog()
//            .getResources(ResourceInfo.class).get(0);
//
//        login();
//        tester.startPage(new ResourceConfigurationPage(info, false));
//        FormTester ft = tester.newFormTester("resource");
//        ft.setValue("tabs:panel:theList:0:content:title", "Some other title");
//        ft.submit();
//        assertEquals("Some other title", ft.getTextComponentValue("tabs:panel:theList:0:content:title"));
//        tester.assertModelValue("resource:tabs:panel:theList:0:content:title", "Some other title");
//        
//        // switch to the other page
//        tester.clickLink("resource:tabs:tabs-container:tabs:1:link");
//        tester.assertRenderedPage(ResourceConfigurationPage.class);
//        tester.assertComponent("resource:tabs:panel:theList:0:content", BasicLayerConfig.class);
//        
//        // switch back
//        tester.clickLink("resource:tabs:tabs-container:tabs:0:link");
//        tester.assertRenderedPage(ResourceConfigurationPage.class);
//        tester.assertComponent("resource:tabs:panel:theList:0:content", BasicResourceConfig.class);
//        
//        // check the title is still what we did set (switching tabs did not make us loose the value)
//        tester.assertComponent("resource:tabs:panel:theList:0:content:title", TextField.class);
//        ft = tester.newFormTester("resource");
//        tester.assertModelValue("resource:tabs:panel:theList:0:content:title", "Some other title");
//    }
}
