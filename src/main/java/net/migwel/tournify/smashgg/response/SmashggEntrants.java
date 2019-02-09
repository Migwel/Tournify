package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggEntrants {

    private List<SmashggNode> nodes;
    private SmashggPageInfo pageInfo;

    public List<SmashggNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<SmashggNode> nodes) {
        this.nodes = nodes;
    }

    public SmashggPageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(SmashggPageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
