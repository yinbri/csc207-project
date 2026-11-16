package entity;

/**
 * The spending categories an expense can belong to.
 */
public enum Category {
    RENT("Rent"),
    FOOD("Food"),
    TRANSPORTATION("Transportation"),
    TUITION("Tuition"),
    ENTERTAINMENT("Entertainment"),
    OTHER("Other");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Looks up a category by its display name (case-insensitive).
     * @param name the display name, e.g. "Food"
     * @return the matching category
     * @throws IllegalArgumentException if no category has that name
     */
    public static Category fromDisplayName(String name) {
        for (Category category : values()) {
            if (category.displayName.equalsIgnoreCase(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + name);
    }
}
