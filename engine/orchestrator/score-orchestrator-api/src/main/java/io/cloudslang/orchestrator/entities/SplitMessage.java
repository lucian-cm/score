/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

package io.cloudslang.orchestrator.entities;

import io.cloudslang.score.facade.entities.Execution;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * User: zruya
 * Date: 08/09/13
 * Time: 17:21
 */
public class SplitMessage implements Message {
	private static final long serialVersionUID = -720851148155732731L;

    private int basicSplitWeight = Integer.getInteger("basic.split.weight",3);

	private final String splitId;
    private final Execution parent;
    private final List<Execution> children;

    public SplitMessage(String splitId, Execution parent, List<Execution> children) {
        Validate.notNull(splitId, "splitId cannot be null");
        Validate.notNull(parent, "parent cannot be null");
        Validate.notNull(children, "children cannot be null");
        Validate.notEmpty(children, "cannot create a split message without any children");

        this.splitId = splitId;
        this.parent = parent;
        this.children = new ArrayList<>(children);
    }

    public Execution getParent() {
        return parent;
    }

    public List<Execution> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public String getSplitId() {
        return splitId;
    }

	@Override
	public int getWeight() {
		return children.size() * basicSplitWeight;
	}

	@Override
	public String getId() {
		return parent.getExecutionId().toString() + parent.getSystemContext().getBranchId();
	}

	@Override
	public List<Message> shrink(List<Message> messages) {
		return messages; // do nothing
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SplitMessage)) return false;

        SplitMessage that = (SplitMessage) o;

        return new EqualsBuilder()
                .append(this.splitId, that.splitId)
                .append(this.parent, that.parent)
                .append(this.children, that.children)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
		        this.splitId,
		        this.parent,
		        this.children);
    }
}
