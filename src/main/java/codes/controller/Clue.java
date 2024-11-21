package codes.controller;

public enum Clue {
    RANDOM_NUMBER_IS_LOWER_THAN_50,
    RANDOM_NUMBER_IS_UPPER_THAN_50,
    NO_NEED_ALREADY_FOUND;

    public Clue giveMeClue(int number) {
        Clue comparaison = Clue.NO_NEED_ALREADY_FOUND;
        if (number<50){
            comparaison = RANDOM_NUMBER_IS_LOWER_THAN_50;
        }
        else if (number>50){
            comparaison = RANDOM_NUMBER_IS_UPPER_THAN_50;
        }
        return comparaison;
    }
}
