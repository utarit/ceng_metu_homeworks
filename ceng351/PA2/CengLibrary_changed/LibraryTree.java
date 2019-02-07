import java.util.ArrayList;

public class LibraryTree {

	public LibraryNode primaryRoot; // root of the primary B+ tree
	public LibraryNode secondaryRoot; // root of the secondary B+ tree

	public LibraryTree(Integer order) {
		LibraryNode.order = order;
		primaryRoot = new LibraryNodeLeaf(null);
		primaryRoot.level = 0;
		secondaryRoot = new LibraryNodeLeaf(null);
		secondaryRoot.level = 0;
	}

	public void addBook(CengBook book) {
		addBookToPrimaryTree(book);
		addBookToSecondaryTree(book);
	}

	public CengBook searchBook(Integer key) {
		searchHelper(primaryRoot, key);

		// add methods to find the book with the searched key in primary B+ tree
		// return value will not be tested, just print as the specicifications say
		return null;
	}

	public void printPrimaryLibrary() {

		traversePrimaryIndex(primaryRoot);
		// add methods to print the primary B+ tree in Depth-first order

	}

	public void printSecondaryLibrary() {

		traverseSecondaryIndex(secondaryRoot);

		// add methods to print the secondary B+ tree in Depth-first order

	}

	// Extra functions if needed
	public void addBookToPrimaryTree(CengBook book) {
		{
			Integer max_limit = 2 * LibraryNode.order;

			/*
			 * LibraryNode node = findLeaftoInsert(primaryRoot, book.key()); LibraryNodeLeaf
			 * leaf = ((LibraryNodeLeaf) node);
			 */
			LibraryNodeLeaf leaf = findLeaftoInsert(primaryRoot, book.key());

			Integer book_key = book.key();

			if (leaf.bookCount() == 0 || book_key < leaf.bookKeyAtIndex(0)) {
				leaf.addBook(0, book);
			} else if (book_key >= leaf.bookKeyAtIndex(leaf.bookCount() - 1)) {
				leaf.addBook(leaf.bookCount(), book);
			} else {
				for (int i = 0; i < leaf.bookCount(); i++) {
					if (leaf.bookKeyAtIndex(i) >= book_key) {
						leaf.addBook(i, book);
						break;
					}
				}
			}

			if (leaf.bookCount() > max_limit) {
				/*
				 * LibraryNode temp_node1 = new LibraryNodeLeaf(leaf.getParent()); LibraryNode
				 * temp_node2 = new LibraryNodeLeaf(leaf.getParent()); LibraryNodeLeaf
				 * right_child = ((LibraryNodeLeaf) temp_node1); LibraryNodeLeaf left_child =
				 * ((LibraryNodeLeaf) temp_node2);
				 */
				LibraryNodeLeaf right_child = new LibraryNodeLeaf(leaf.getParent());
				LibraryNodeLeaf left_child = new LibraryNodeLeaf(leaf.getParent());

				int y = 0;

				for (int i = 0; i < LibraryNode.order; i++) {
					left_child.addBook(y++, leaf.bookAtIndex(i));
				}
				y = 0;
				for (int i = LibraryNode.order; i <= max_limit; i++) {
					right_child.addBook(y++, leaf.bookAtIndex(i));
				}

				if (leaf.getParent() == null) {
					/*
					 * LibraryNode new_root_tmp = new LibraryNodePrimaryIndex(null);
					 * LibraryNodePrimaryIndex new_root = ((LibraryNodePrimaryIndex) new_root_tmp);
					 */
					LibraryNodePrimaryIndex new_root = new LibraryNodePrimaryIndex(null);

					new_root.addKey(0, right_child.bookKeyAtIndex(0));
					new_root.addChild(0, left_child);
					new_root.addChild(1, right_child);
					left_child.setParent(new_root);
					right_child.setParent(new_root);
					primaryRoot = new_root;
				} else {
					LibraryNode parent = leaf.getParent();
					left_child.setParent(parent);
					right_child.setParent(parent);
					Integer first_key_of_right_child = right_child.bookKeyAtIndex(0);
					Integer keySize = ((LibraryNodePrimaryIndex) parent).keyCount();

					if (first_key_of_right_child < ((LibraryNodePrimaryIndex) parent).keyAtIndex(0)) {
						((LibraryNodePrimaryIndex) parent).addKey(0, first_key_of_right_child);
						((LibraryNodePrimaryIndex) parent).removeChild(0);
						((LibraryNodePrimaryIndex) parent).addChild(0, left_child);
						((LibraryNodePrimaryIndex) parent).addChild(1, right_child);

					} else if (first_key_of_right_child >= ((LibraryNodePrimaryIndex) parent).keyAtIndex(keySize - 1)) {
						((LibraryNodePrimaryIndex) parent).addKey(keySize, first_key_of_right_child);
						((LibraryNodePrimaryIndex) parent).removeChild(keySize);
						((LibraryNodePrimaryIndex) parent).addChild(keySize, left_child);
						((LibraryNodePrimaryIndex) parent).addChild(keySize + 1, right_child);

					} else {
						for (int i = 0; i < keySize; i++) {
							if (((LibraryNodePrimaryIndex) parent).keyAtIndex(i) >= first_key_of_right_child) {
								((LibraryNodePrimaryIndex) parent).addKey(i, first_key_of_right_child);
								((LibraryNodePrimaryIndex) parent).removeChild(i);
								((LibraryNodePrimaryIndex) parent).addChild(i, left_child);
								((LibraryNodePrimaryIndex) parent).addChild(i + 1, right_child);
								break;
							}

						}
					}

					if (((LibraryNodePrimaryIndex) parent).keyCount() > max_limit) {
						splitParentsUp(((LibraryNodePrimaryIndex) parent));
					}

				}

			}

		}
	}

	public void addBookToSecondaryTree(CengBook book) {
		{
			Integer max_limit = 2 * LibraryNode.order;

			/*
			 * LibraryNode node = findLeaftoInsert(primaryRoot, book.key()); LibraryNodeLeaf
			 * leaf = ((LibraryNodeLeaf) node);
			 */
			LibraryNodeLeaf leaf = findLeaftoInsertSecondary(secondaryRoot, book.key(), book.year());

			if (leaf.bookCount() == 0 || is_small(book, leaf, 0)) {
				leaf.addBook(0, book);
			} else if (!is_small(book, leaf, leaf.bookCount() - 1)) {
				leaf.addBook(leaf.bookCount(), book);
			} else {
				for (int i = 0; i < leaf.bookCount(); i++) {
					if (is_small(book, leaf, i)) {
						leaf.addBook(i, book);
						break;
					}
				}
			}

			if (leaf.bookCount() > max_limit) {
				/*
				 * LibraryNode temp_node1 = new LibraryNodeLeaf(leaf.getParent()); LibraryNode
				 * temp_node2 = new LibraryNodeLeaf(leaf.getParent()); LibraryNodeLeaf
				 * right_child = ((LibraryNodeLeaf) temp_node1); LibraryNodeLeaf left_child =
				 * ((LibraryNodeLeaf) temp_node2);
				 */
				LibraryNodeLeaf right_child = new LibraryNodeLeaf(leaf.getParent());
				LibraryNodeLeaf left_child = new LibraryNodeLeaf(leaf.getParent());

				int y = 0;

				for (int i = 0; i < LibraryNode.order; i++) {
					left_child.addBook(y++, leaf.bookAtIndex(i));
				}
				y = 0;
				for (int i = LibraryNode.order; i <= max_limit; i++) {
					right_child.addBook(y++, leaf.bookAtIndex(i));
				}

				if (leaf.getParent() == null) {
					/*
					 * LibraryNode new_root_tmp = new LibraryNodePrimaryIndex(null);
					 * LibraryNodePrimaryIndex new_root = ((LibraryNodePrimaryIndex) new_root_tmp);
					 */
					LibraryNodeSecondaryIndex new_root = new LibraryNodeSecondaryIndex(null);

					new_root.addKey(0, right_child.bookKeyAtIndex(0));
					new_root.addYear(0, right_child.bookYearAtIndex(0));
					new_root.addChild(0, left_child);
					new_root.addChild(1, right_child);
					left_child.setParent(new_root);
					right_child.setParent(new_root);
					secondaryRoot = new_root;
				} else {
					LibraryNode parent = leaf.getParent();
					left_child.setParent(parent);
					right_child.setParent(parent);
					Integer first_key_of_right_child = right_child.bookKeyAtIndex(0);
					Integer first_year_of_right_child = right_child.bookYearAtIndex(0);
					Integer keySize = ((LibraryNodeSecondaryIndex) parent).keyCount();

					if (is_small(right_child.bookAtIndex(0), ((LibraryNodeSecondaryIndex) parent), 0)) {
						((LibraryNodeSecondaryIndex) parent).addKey(0, first_key_of_right_child);
						((LibraryNodeSecondaryIndex) parent).addYear(0, first_year_of_right_child);
						((LibraryNodeSecondaryIndex) parent).removeChild(0);
						((LibraryNodeSecondaryIndex) parent).addChild(0, left_child);
						((LibraryNodeSecondaryIndex) parent).addChild(1, right_child);

					} else if (!is_small(right_child.bookAtIndex(0), ((LibraryNodeSecondaryIndex) parent),
							keySize - 1)) {
						((LibraryNodeSecondaryIndex) parent).addKey(keySize, first_key_of_right_child);
						((LibraryNodeSecondaryIndex) parent).addYear(keySize, first_year_of_right_child);
						((LibraryNodeSecondaryIndex) parent).removeChild(keySize);
						((LibraryNodeSecondaryIndex) parent).addChild(keySize, left_child);
						((LibraryNodeSecondaryIndex) parent).addChild(keySize + 1, right_child);

					} else {
						for (int i = 0; i < keySize; i++) {
							if (is_small(right_child.bookAtIndex(0), ((LibraryNodeSecondaryIndex) parent), i)) {
								((LibraryNodeSecondaryIndex) parent).addKey(i, first_key_of_right_child);
								((LibraryNodeSecondaryIndex) parent).addYear(i, first_year_of_right_child);
								((LibraryNodeSecondaryIndex) parent).removeChild(i);
								((LibraryNodeSecondaryIndex) parent).addChild(i, left_child);
								((LibraryNodeSecondaryIndex) parent).addChild(i + 1, right_child);
								break;
							}

						}
					}

					if (((LibraryNodeSecondaryIndex) parent).keyCount() > max_limit) {
						splitParentsUpSecondary(((LibraryNodeSecondaryIndex) parent));
					}

				}

			}

		}
	}

	public Boolean searchHelper(LibraryNode node, Integer key) {

		if (node.getType() == LibraryNodeType.Leaf) {
			ArrayList<CengBook> books = ((LibraryNodeLeaf) node).getbooks();
			for (int i = 0; i < books.size(); i++) {
				CengBook book = books.get(i);
				//System.out.println("DBG: " + book.key());
				if (key == book.key()) {
					System.out.println("<data>");
					System.out.println("<record>" + book.key() + "|" + book.year() + "|" + book.name() + "|"
							+ book.author() + "</record>");
					System.out.println("</data>");
					return true;
				}
			}
			System.out.println("No match for " + key);
			return false;
		} else {
			ArrayList<Integer> keys = ((LibraryNodePrimaryIndex) node).keys();
			ArrayList<LibraryNode> children = ((LibraryNodePrimaryIndex) node).getAllChildren();
			System.out.println("<index>");
			for (int i = 0; i < keys.size(); i++) {
				System.out.println(keys.get(i));
			}
			System.out.println("</index>");
			if (key < keys.get(0)) {
				//System.out.println("ENTER: 1");
				searchHelper(children.get(0), key);
			} else if (key >= keys.get(keys.size() - 1)) {
				//System.out.println("ENTER: 2");
				searchHelper(children.get(children.size() - 1), key);
			} else {
				for (int i = 0; i < keys.size(); i++) {
					if (keys.get(i) > key) {
						//System.out.println("ENTER: 3");
						searchHelper(children.get(i), key);
						break;
					}
				}
			}
		}

		return null;
	}

	public LibraryNodeLeaf findLeaftoInsert(LibraryNode node, Integer key) {
		if (node.getType() == LibraryNodeType.Leaf) {
			return ((LibraryNodeLeaf) node); // Bunu değiştirmek gerekebilir
		} else {
			LibraryNodePrimaryIndex tmp_internal = ((LibraryNodePrimaryIndex) node);
			Integer keyCount = tmp_internal.keyCount();
			if (key < tmp_internal.keyAtIndex(0))
				return findLeaftoInsert(tmp_internal.getChildrenAt(0), key);
			else if (key >= tmp_internal.keyAtIndex(keyCount - 1))
				return findLeaftoInsert(tmp_internal.getChildrenAt(keyCount), key);
			else {
				for (int i = 0; i < keyCount; i++) {
					if (tmp_internal.keyAtIndex(i) >= key) {
						return findLeaftoInsert(tmp_internal.getChildrenAt(i), key);
					}
				}
			}

		}
		return null;
	}

	public LibraryNodeLeaf findLeaftoInsertSecondary(LibraryNode node, Integer key, Integer year) {
		if (node.getType() == LibraryNodeType.Leaf) {
			return ((LibraryNodeLeaf) node); // Bunu değiştirmek gerekebilir
		} else {
			LibraryNodeSecondaryIndex tmp_internal = ((LibraryNodeSecondaryIndex) node);
			Integer keyCount = tmp_internal.keyCount();
			if (year < tmp_internal.yearAtIndex(0) || year == tmp_internal.yearAtIndex(0) && key < tmp_internal.keyAtIndex(0))
				return findLeaftoInsertSecondary(tmp_internal.getChildrenAt(0), key, year);
			else if (year >= tmp_internal.yearAtIndex(keyCount - 1) || (year == tmp_internal.yearAtIndex(keyCount - 1) && key > tmp_internal.keyAtIndex(keyCount - 1)))
				return findLeaftoInsertSecondary(tmp_internal.getChildrenAt(keyCount), key, year);
			else {
				for (int i = 0; i < keyCount; i++) {
					if (year < tmp_internal.yearAtIndex(i) || year == tmp_internal.yearAtIndex(i) && key <= tmp_internal.keyAtIndex(i)) {
						return findLeaftoInsertSecondary(tmp_internal.getChildrenAt(i), key, year);
					}
				}
			}

		}
		return null;
	}

	public void traversePrimaryIndex(LibraryNode node) {
		if (node.getType() == LibraryNodeType.Leaf) {
			ArrayList<CengBook> books = ((LibraryNodeLeaf) node).getbooks();
			System.out.println("<data>");
			for (int i = 0; i < books.size(); i++) {
				CengBook book = books.get(i);
				System.out.println("<record>" + book.key() + "|" + book.year() + "|" + book.name() + "|" + book.author()
						+ "</record>");
			}
			System.out.println("</data>");
		} else {
			ArrayList<Integer> keys = ((LibraryNodePrimaryIndex) node).keys();
			ArrayList<LibraryNode> children = ((LibraryNodePrimaryIndex) node).getAllChildren();
			System.out.println("<index>");
			for (int i = 0; i < keys.size(); i++) {
				System.out.println(keys.get(i));
			}
			System.out.println("</index>");
			for (int i = 0; i < children.size(); i++) {
				traversePrimaryIndex(children.get(i));
			}
		}
	}

	public void traverseSecondaryIndex(LibraryNode node) {
		if (node.getType() == LibraryNodeType.Leaf) {
			ArrayList<CengBook> books = ((LibraryNodeLeaf) node).getbooks();
			System.out.println("<data>");
			for (int i = 0; i < books.size(); i++) {
				CengBook book = books.get(i);
				System.out.println("<record>" + book.key() + "|" + book.year() + "|" + book.name() + "|" + book.author()
						+ "</record>");
			}
			System.out.println("</data>");
		} else {
			ArrayList<Integer> keys = ((LibraryNodeSecondaryIndex) node).keys();
			ArrayList<Integer> years = ((LibraryNodeSecondaryIndex) node).years();
			ArrayList<LibraryNode> children = ((LibraryNodeSecondaryIndex) node).getAllChildren();
			System.out.println("<index>");
			for (int i = 0; i < keys.size(); i++) {
				System.out.println(years.get(i) + "|" + keys.get(i));
			}
			System.out.println("</index>");
			for (int i = 0; i < children.size(); i++) {
				traverseSecondaryIndex(children.get(i));
			}
		}
	}

	public void splitParentsUp(LibraryNodePrimaryIndex node) {
		Integer max_limit = 2 * LibraryNode.order;

		LibraryNodePrimaryIndex right_child = new LibraryNodePrimaryIndex(node.getParent());
		LibraryNodePrimaryIndex left_child = new LibraryNodePrimaryIndex(node.getParent());
		Integer key_to_parent = node.keyAtIndex(LibraryNode.order);
		LibraryNode tmp_grandChild;
		int y = 0;

		for (int i = 0; i < LibraryNode.order; i++) {
			left_child.addKey(y, node.keyAtIndex(i));
			tmp_grandChild = node.getChildrenAt(i);
			left_child.addChild(y, tmp_grandChild);
			tmp_grandChild.setParent(left_child);
			y++;
		}
		tmp_grandChild = node.getChildrenAt(LibraryNode.order);
		left_child.addChild(y, node.getChildrenAt(LibraryNode.order));
		tmp_grandChild.setParent(left_child);
		y = 0;

		for (int i = LibraryNode.order + 1; i <= max_limit; i++) {
			right_child.addKey(y, node.keyAtIndex(i));
			tmp_grandChild = node.getChildrenAt(i);
			right_child.addChild(y, node.getChildrenAt(i));
			tmp_grandChild.setParent(right_child);
			y++;
		}
		tmp_grandChild = node.getChildrenAt(max_limit + 1);
		right_child.addChild(y, node.getChildrenAt(max_limit + 1));
		tmp_grandChild.setParent(right_child);

		if (node.getParent() == null) {
			/*
			 * LibraryNode new_root_tmp = new LibraryNodePrimaryIndex(null);
			 * LibraryNodePrimaryIndex new_root = ((LibraryNodePrimaryIndex) new_root_tmp);
			 */
			LibraryNodePrimaryIndex new_root = new LibraryNodePrimaryIndex(null);

			new_root.addKey(0, key_to_parent);
			new_root.addChild(0, left_child);
			new_root.addChild(1, right_child);
			left_child.setParent(new_root);
			right_child.setParent(new_root);
			primaryRoot = new_root;
		} else {
			LibraryNode parent = node.getParent();
			left_child.setParent(parent);
			right_child.setParent(parent);
			Integer keySize = ((LibraryNodePrimaryIndex) parent).keyCount();

			if (key_to_parent < ((LibraryNodePrimaryIndex) parent).keyAtIndex(0)) {
				((LibraryNodePrimaryIndex) parent).addKey(0, key_to_parent);
				((LibraryNodePrimaryIndex) parent).removeChild(0);
				((LibraryNodePrimaryIndex) parent).addChild(0, left_child);
				((LibraryNodePrimaryIndex) parent).addChild(1, right_child);

			} else if (key_to_parent >= ((LibraryNodePrimaryIndex) parent).keyAtIndex(keySize - 1)) {
				((LibraryNodePrimaryIndex) parent).addKey(keySize, key_to_parent);
				((LibraryNodePrimaryIndex) parent).removeChild(keySize);
				((LibraryNodePrimaryIndex) parent).addChild(keySize, left_child);
				((LibraryNodePrimaryIndex) parent).addChild(keySize + 1, right_child);

			} else {
				for (int i = 0; i < keySize; i++) {
					if (((LibraryNodePrimaryIndex) parent).keyAtIndex(i) >= key_to_parent) {
						((LibraryNodePrimaryIndex) parent).addKey(i, key_to_parent);
						((LibraryNodePrimaryIndex) parent).removeChild(i);
						((LibraryNodePrimaryIndex) parent).addChild(i, left_child);
						((LibraryNodePrimaryIndex) parent).addChild(i + 1, right_child);
						break;
					}

				}
			}

			if (((LibraryNodePrimaryIndex) parent).keyCount() > max_limit) {
				splitParentsUp(((LibraryNodePrimaryIndex) parent));
			}

		}
	}

	public void splitParentsUpSecondary(LibraryNodeSecondaryIndex node) {
		Integer max_limit = 2 * LibraryNode.order;

		LibraryNodeSecondaryIndex right_child = new LibraryNodeSecondaryIndex(node.getParent());
		LibraryNodeSecondaryIndex left_child = new LibraryNodeSecondaryIndex(node.getParent());
		Integer index_to_parent = LibraryNode.order;
		LibraryNode tmp_grandChild;
		int y = 0;

		for (int i = 0; i < LibraryNode.order; i++) {
			left_child.addKey(y, node.keyAtIndex(i));
			left_child.addYear(y, node.yearAtIndex(i));
			tmp_grandChild = node.getChildrenAt(i);
			left_child.addChild(y, tmp_grandChild);
			tmp_grandChild.setParent(left_child);
			y++;
		}
		tmp_grandChild = node.getChildrenAt(LibraryNode.order);
		left_child.addChild(y, node.getChildrenAt(LibraryNode.order));
		tmp_grandChild.setParent(left_child);
		y = 0;

		for (int i = LibraryNode.order + 1; i <= max_limit; i++) {
			right_child.addKey(y, node.keyAtIndex(i));
			right_child.addYear(y, node.yearAtIndex(i));
			tmp_grandChild = node.getChildrenAt(i);
			right_child.addChild(y, node.getChildrenAt(i));
			tmp_grandChild.setParent(right_child);
			y++;
		}
		tmp_grandChild = node.getChildrenAt(max_limit + 1);
		right_child.addChild(y, node.getChildrenAt(max_limit + 1));
		tmp_grandChild.setParent(right_child);

		if (node.getParent() == null) {
			/*
			 * LibraryNode new_root_tmp = new LibraryNodeSecondaryIndex(null);
			 * LibraryNodeSecondaryIndex new_root = ((LibraryNodeSecondaryIndex)
			 * new_root_tmp);
			 */
			LibraryNodeSecondaryIndex new_root = new LibraryNodeSecondaryIndex(null);

			new_root.addKey(0, node.keyAtIndex(index_to_parent));
			new_root.addYear(0, node.yearAtIndex(index_to_parent));
			new_root.addChild(0, left_child);
			new_root.addChild(1, right_child);
			left_child.setParent(new_root);
			right_child.setParent(new_root);
			secondaryRoot = new_root;
		} else {
			LibraryNode parent = node.getParent();
			left_child.setParent(parent);
			right_child.setParent(parent);
			Integer keySize = ((LibraryNodeSecondaryIndex) parent).keyCount();

			if (is_small(node, index_to_parent, ((LibraryNodeSecondaryIndex) parent), 0)) {
				((LibraryNodeSecondaryIndex) parent).addKey(0, node.keyAtIndex(index_to_parent));
				((LibraryNodeSecondaryIndex) parent).addYear(0, node.yearAtIndex(index_to_parent));
				((LibraryNodeSecondaryIndex) parent).removeChild(0);
				((LibraryNodeSecondaryIndex) parent).addChild(0, left_child);
				((LibraryNodeSecondaryIndex) parent).addChild(1, right_child);

			} else if (!is_small(node, index_to_parent, ((LibraryNodeSecondaryIndex) parent), keySize - 1)) {
				((LibraryNodeSecondaryIndex) parent).addKey(keySize, node.keyAtIndex(index_to_parent));
				((LibraryNodeSecondaryIndex) parent).addYear(keySize, node.yearAtIndex(index_to_parent));
				((LibraryNodeSecondaryIndex) parent).removeChild(keySize);
				((LibraryNodeSecondaryIndex) parent).addChild(keySize, left_child);
				((LibraryNodeSecondaryIndex) parent).addChild(keySize + 1, right_child);

			} else {
				for (int i = 0; i < keySize; i++) {
					if (is_small(node, index_to_parent, ((LibraryNodeSecondaryIndex) parent), i)) {
						((LibraryNodeSecondaryIndex) parent).addKey(i, node.keyAtIndex(index_to_parent));
						((LibraryNodeSecondaryIndex) parent).addYear(i, node.yearAtIndex(index_to_parent));
						((LibraryNodeSecondaryIndex) parent).removeChild(i);
						((LibraryNodeSecondaryIndex) parent).addChild(i, left_child);
						((LibraryNodeSecondaryIndex) parent).addChild(i + 1, right_child);
						break;
					}

				}
			}

			/*
			 * if (key_to_parent < ((LibraryNodeSecondaryIndex) parent).keyAtIndex(0)) {
			 * ((LibraryNodeSecondaryIndex) parent).addKey(0, key_to_parent);
			 * ((LibraryNodeSecondaryIndex) parent).removeChild(0);
			 * ((LibraryNodeSecondaryIndex) parent).addChild(0, left_child);
			 * ((LibraryNodeSecondaryIndex) parent).addChild(1, right_child);
			 * 
			 * } else if (key_to_parent >= ((LibraryNodeSecondaryIndex)
			 * parent).keyAtIndex(keySize - 1)) { ((LibraryNodeSecondaryIndex)
			 * parent).addKey(keySize, key_to_parent); ((LibraryNodeSecondaryIndex)
			 * parent).removeChild(keySize); ((LibraryNodeSecondaryIndex)
			 * parent).addChild(keySize, left_child); ((LibraryNodeSecondaryIndex)
			 * parent).addChild(keySize + 1, right_child);
			 * 
			 * } else { for (int i = 0; i < keySize; i++) { if (((LibraryNodeSecondaryIndex)
			 * parent).keyAtIndex(i) >= key_to_parent) { ((LibraryNodeSecondaryIndex)
			 * parent).addKey(i, key_to_parent); ((LibraryNodeSecondaryIndex)
			 * parent).removeChild(i); ((LibraryNodeSecondaryIndex) parent).addChild(i,
			 * left_child); ((LibraryNodeSecondaryIndex) parent).addChild(i + 1,
			 * right_child); break; }
			 * 
			 * } }
			 */

			if (((LibraryNodeSecondaryIndex) parent).keyCount() > max_limit) {
				splitParentsUpSecondary(((LibraryNodeSecondaryIndex) parent));
			}

		}
	}

	public Boolean is_small(CengBook book, LibraryNode node, Integer index) {
		if (node.getType() == LibraryNodeType.Leaf) {
			if (book.year() < ((LibraryNodeLeaf) node).bookYearAtIndex(index)) {
				return true;
			} else if (book.year() > ((LibraryNodeLeaf) node).bookYearAtIndex(index)) {
				return false;
			} else {
				if (book.key() < ((LibraryNodeLeaf) node).bookKeyAtIndex(index)) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			if (book.year() < ((LibraryNodeSecondaryIndex) node).yearAtIndex(index)) {
				return true;
			} else if (book.year() > ((LibraryNodeSecondaryIndex) node).yearAtIndex(index)) {
				return false;
			} else {
				if (book.key() < ((LibraryNodeSecondaryIndex) node).keyAtIndex(index)) {
					return true;
				} else {
					return false;
				}
			}
		}

	}

	public Boolean is_small(LibraryNodeSecondaryIndex the_node, Integer i, LibraryNode node, Integer index) {
		if (node.getType() == LibraryNodeType.Leaf) {
			if (the_node.yearAtIndex(i)< ((LibraryNodeLeaf) node).bookYearAtIndex(index)) {
				return true;
			} else if (the_node.yearAtIndex(i) > ((LibraryNodeLeaf) node).bookYearAtIndex(index)) {
				return false;
			} else {
				if (the_node.keyAtIndex(i)< ((LibraryNodeLeaf) node).bookKeyAtIndex(index)) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			if (the_node.yearAtIndex(i) < ((LibraryNodeSecondaryIndex) node).yearAtIndex(index)) {
				return true;
			} else if (the_node.yearAtIndex(i) > ((LibraryNodeSecondaryIndex) node).yearAtIndex(index)) {
				return false;
			} else {
				if (the_node.keyAtIndex(i) < ((LibraryNodeSecondaryIndex) node).keyAtIndex(index)) {
					return true;
				} else {
					return false;
				}
			}
		}

	}

}
