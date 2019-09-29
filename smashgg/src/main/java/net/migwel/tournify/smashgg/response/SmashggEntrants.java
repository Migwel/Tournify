package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggEntrants {

    private Collection<SmashggNode> nodes;
    private SmashggPageInfo pageInfo;

    public Collection<SmashggNode> getNodes() {
        return nodes;
    }

    public void setNodes(Collection<SmashggNode> nodes) {
        this.nodes = nodes;
    }

    public SmashggPageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(SmashggPageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
