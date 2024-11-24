package codes.controller;

public enum ClueEnum {
    RANDOM_NUMBER_IS_LOWER_THAN_50,
    RANDOM_NUMBER_IS_UPPER_THAN_50,
    NO_NEED_ALREADY_FOUND;

    public ClueEnum giveMeClue(int number) {
        ClueEnum comparaison = ClueEnum.NO_NEED_ALREADY_FOUND;
        if (number<50){
            comparaison = RANDOM_NUMBER_IS_LOWER_THAN_50;
        }
        else if (number>50){
            comparaison = RANDOM_NUMBER_IS_UPPER_THAN_50;
        }
        return comparaison;
    }
}
