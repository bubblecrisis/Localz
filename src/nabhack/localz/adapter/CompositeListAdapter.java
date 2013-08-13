/*
 * (C) 2013 National Australia Bank
 *
 * [All rights reserved]. This product and related documentation are protected
 * by copyright restricting its use, copying, distribution, and decompilation.
 * No part of this product or related documentation may be reproduced in any
 * form by any means without prior written authorization of
 * National Australia Bank. Unless otherwise arranged, third parties may not
 * have access to this product or related documents.
 */
package nabhack.localz.adapter;

import nabhack.localz.ui.compositelist.Row;

/**
 * This interface is used by adapters that are made up of different objects.
 * 
 * @author markng
 */
public interface CompositeListAdapter {
	
	
	/**
     * Add new row to adapter.
     * @param row row to add.
     */
    void add(Row row);

    /**
     * Get the count of objects stored in the adapter.
     * @return the count.
     */
    int getCount();
    
    /**
     * Get row.
     * @param position the position in the list
     * @return the row data
     */
    Row getItem(int position);
    
}
