package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggPaginatedSets {
    private SmashggPageInfo pageInfo;
    private Collection<SmashggNode> nodes;

    public SmashggPageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(SmashggPageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public Collection<SmashggNode> getNodes() {
        return nodes;
    }

    public void setNodes(Collection<SmashggNode> nodes) {
        this.nodes = nodes;
    }
}
