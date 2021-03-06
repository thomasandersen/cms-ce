<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE xsl:stylesheet [
	<!ENTITY nbsp "&#160;">
]>
<xsl:stylesheet version="1.0" exclude-result-prefixes="#all"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:exslt-common="http://exslt.org/common"
                xmlns:saxon="http://saxon.sf.net/"
                xmlns:admin="java:com.enonic.cms.core.xslt.lib.AdminFunctions">

    <xsl:output method="html"/>

    <xsl:include href="common/generic_parameters.xsl"/>
    <xsl:include href="common/tablecolumnheader.xsl"/>
    <xsl:include href="common/tablerowpainter.xsl"/>
    <xsl:include href="common/displaysystempath.xsl"/>
    <xsl:include href="common/waitsplash.xsl"/>
    <xsl:include href="common/button.xsl"/>
    <xsl:include href="common/formatdate.xsl"/>
    <xsl:include href="common/operations_template.xsl"/>
    <xsl:include href="common/browse_table_js.xsl"/>

    <xsl:param name="sortby" select="'name'"/>
    <xsl:param name="sortby-direction" select="'ascending'"/>

    <xsl:variable name="pageURL">
        <xsl:text>adminpage?page=</xsl:text><xsl:value-of select="$page"/>
        <xsl:text>&amp;op=browse</xsl:text>
    </xsl:variable>

    <xsl:template match="/">
        <html>

            <head>
            	<xsl:call-template name="waitsplash"/>
                <link href="css/admin.css" rel="stylesheet" type="text/css">
                </link>
                <script type="text/javascript" src="javascript/admin.js">//</script>
            </head>


            <body>
                <h1>
                    <xsl:call-template name="displaysystempath">
                        <xsl:with-param name="page" select="$page"/>
                    </xsl:call-template>
                </h1>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td class="browse_title_buttonrow_seperator"><img src="images/1x1.gif"/></td>
                    </tr>
                    <tr>
                        <td>
                            <xsl:call-template name="button">
                                <xsl:with-param name="type" select="'link'" />
                                <xsl:with-param name="caption" select="'%cmdNew%'" />
                                <xsl:with-param name="href">
                                    <xsl:text>adminpage?page=</xsl:text>
                                    <xsl:value-of select="$page"/>
                                    <xsl:text>&amp;op=form</xsl:text>
                                </xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
                    <tr>
                        <td class="browse_buttonrow_datarows_seperator"><img src="images/1x1.gif"/></td>
                    </tr>
                    <tr>
                        <td>
                            <table width="100%" cellspacing="0" cellpadding="0" class="browsetable">
                                <tr>
                                    <xsl:call-template name="tablecolumnheader">
                                        <xsl:with-param name="caption" select="'%fldName%'" />
                                        <xsl:with-param name="pageURL" select="$pageURL" />
                                        <xsl:with-param name="current-sortby" select="$sortby" />
                                        <xsl:with-param name="current-sortby-direction" select="$sortby-direction" />
                                        <xsl:with-param name="sortby" select="'name'" />
                                    </xsl:call-template>

									<xsl:call-template name="tablecolumnheader">
                                        <xsl:with-param name="width" select="'120'" />
                                        <xsl:with-param name="caption" select="'%fldClass%'" />
                                        <xsl:with-param name="pageURL" select="$pageURL" />
                                        <xsl:with-param name="current-sortby" select="$sortby" />
                                        <xsl:with-param name="current-sortby-direction" select="$sortby-direction" />
                                        <xsl:with-param name="sortby" select="'class'" />
                                    </xsl:call-template>

                                    <xsl:call-template name="tablecolumnheader">
                                        <xsl:with-param name="width" select="'120'" />
                                        <xsl:with-param name="align" select="'center'" />
                                        <xsl:with-param name="caption" select="'%fldHandlerNo%'" />
                                        <xsl:with-param name="pageURL" select="$pageURL" />
                                        <xsl:with-param name="current-sortby" select="$sortby" />
                                        <xsl:with-param name="current-sortby-direction" select="$sortby-direction" />
                                        <xsl:with-param name="sortby" select="'@key'" />
                                    </xsl:call-template>

                                    <xsl:call-template name="tablecolumnheader">
                                        <xsl:with-param name="width" select="'100'" />
                                        <xsl:with-param name="align" select="'center'" />
                                        <xsl:with-param name="caption" select="'%fldModified%'" />
                                        <xsl:with-param name="pageURL" select="$pageURL" />
                                        <xsl:with-param name="current-sortby" select="$sortby" />
                                        <xsl:with-param name="current-sortby-direction" select="$sortby-direction" />
                                        <xsl:with-param name="sortby" select="'timestamp'" />
                                    </xsl:call-template>

                                    <xsl:call-template name="tablecolumnheader">
                                        <xsl:with-param name="width" select="'90'" />
                                        <xsl:with-param name="caption" select="''" />
                                        <xsl:with-param name="sortable" select="'false'" />
                                    </xsl:call-template>
                                </tr>

                                <xsl:variable name="sortby-data-type">
                                    <xsl:choose>
                                        <xsl:when test="$sortby = '@key'">number</xsl:when>
                                        <xsl:otherwise>text</xsl:otherwise>
                                    </xsl:choose>
                                </xsl:variable>

                                <xsl:for-each select="/contenthandlers/contenthandler">
                                    <xsl:sort data-type="{$sortby-data-type}" order="{$sortby-direction}" select="*[name() = $sortby] | @*[concat('@',name()) = $sortby]"/>

                                  <xsl:variable name="css-class">
                                    <xsl:text>browsetablecell</xsl:text>
                                    <xsl:if test="position() = last()">
                                      <xsl:text> row-last</xsl:text>
                                    </xsl:if>
                                  </xsl:variable>

                                  <tr>
                                      <xsl:call-template name="tablerowpainter"/>
                                      <td class="{$css-class}" title="%msgClickToEdit%">
                                        <xsl:call-template name="addJSEvent">
												<xsl:with-param name="key" select="@key"/>
											</xsl:call-template>
                                            <xsl:value-of select="name"/>
                                        </td>
                                        <td class="{$css-class}" title="%msgClickToEdit%">
											<xsl:call-template name="addJSEvent">
												<xsl:with-param name="key" select="@key"/>
											</xsl:call-template>
                                            <xsl:value-of select="class"/>
                                        </td>
                                        <td align="center" class="{$css-class}" title="%msgClickToEdit%">
											<xsl:call-template name="addJSEvent">
												<xsl:with-param name="key" select="@key"/>
											</xsl:call-template>
                                            <xsl:value-of select="@key"/>
                                        </td>
                                        <td align="center" class="{$css-class}" title="%msgClickToEdit%">
											<xsl:call-template name="addJSEvent">
												<xsl:with-param name="key" select="@key"/>
											</xsl:call-template>
                                            <xsl:call-template name="formatdatetime">
                                                <xsl:with-param name="date" select="timestamp"/>
                                            </xsl:call-template>
                                        </td>
                                        <td align="center" class="{$css-class}">
                                        	<table border="0" cellspacing="0" cellpadding="0">
                                        		<tr>
			                                        <td align="center" style="padding-left: 3; padding-right: 3;">
			                                            <xsl:call-template name="button">
			                                                <xsl:with-param name="style" select="'flat'"/>
			                                                <xsl:with-param name="type" select="'link'"/>
			                                                <xsl:with-param name="id">
			                                                    <xsl:text>operation_regenerateindex_</xsl:text><xsl:value-of select="@key"/>
			                                                </xsl:with-param>
			                                                <xsl:with-param name="image" select="'images/icon_synchronize.gif'"/>
			                                                <xsl:with-param name="href">
			                                                    <xsl:text>adminpage?page=</xsl:text><xsl:value-of select="$page"/>
			                                                    <xsl:text>&amp;op=regenerateindex</xsl:text>
			                                                    <xsl:text>&amp;contenthandlerkey=</xsl:text><xsl:value-of select="@key"/>
			                                                </xsl:with-param>
                                                            <xsl:with-param name="tooltip" select="'%cmdIndex%'"/>
                                                            <xsl:with-param name="onclick">
                                                                <xsl:text>waitsplash();</xsl:text>
                                                            </xsl:with-param>
                                                            <xsl:with-param name="condition">
                                                                <xsl:text>confirm('%alertRegenerateIndexForContentHandler%')</xsl:text>
			                                                </xsl:with-param>
			                                            </xsl:call-template>
			                                        </td>
			                                        <td align="center">
			                                            <xsl:call-template name="operations">
			                                                <xsl:with-param name="page" select="$page"/>
			                                                <xsl:with-param name="key" select="@key"/>
			                                            </xsl:call-template>
			                                        </td>
			                                    </tr>
			                                </table>
                                        </td>
                                    </tr>
                                </xsl:for-each>
                            </table>
                        </td>
                    </tr>
                </table>

            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
