import math


def parse(filename: str) -> dict[int, list[set[tuple[int, str]]]]:
    """
    To regex or to not regex, I choose to not regex because I hate fun.
    """
    with open(filename) as f:
        games: dict[int, list[set[tuple[int, str]]]] = {}

        for line in f.readlines():
            data = line.split(":")
            game_id: int = int(data[0].split()[-1])
            subsets = data[-1].split(";")
            game_data: list[set[tuple[int, str]]] = []
            for ss in subsets:
                cubes = ss.split(",")
                taken_cubes: set[tuple[int, str]] = set()
                for cube in cubes:
                    cube_data = cube.split()
                    num = int(cube_data[0])
                    color = cube_data[-1].strip()
                    taken_cubes.add((num, color))
                game_data.append(taken_cubes)
            games[game_id] = game_data
    return games


def get_max_nums(game_data: list[set[tuple[int, str]]]) -> dict[str, int]:
    """
    Get the maximum number of each color in a game.
    """
    max_dict: dict[str, int] = {}
    for cubes in game_data:
        for num, color in cubes:
            if color not in max_dict:
                max_dict[color] = num
                continue

            max_num = max_dict[color]
            if max_num > num:
                continue

            max_dict[color] = num
    return max_dict


def is_bag_possible(bag: dict[str, int], game_data: list[set[tuple[int, str]]]) -> bool:
    """
    Thank god this isn't a probability question...
    """
    max_dict = get_max_nums(game_data)

    for color, num in max_dict.items():
        if bag[color] < num:
            return False

    return True


def possible_games(
    bag: dict[str, int], games: dict[int, list[set[tuple[int, str]]]]
) -> list[int]:
    possible: list[int] = []
    for id, game_data in games.items():
        if is_bag_possible(bag, game_data):
            possible.append(id)

    return possible


def power_set(bag: dict[str, int]) -> int:
    return math.prod(bag.values())


def main() -> None:
    games = parse("input.txt")

    bag = {"red": 12, "green": 13, "blue": 14}
    pos_games = possible_games(bag, games)
    print("Part 1:", sum(pos_games))

    sum_power = sum(map(lambda g: power_set(get_max_nums(g)), games.values()))
    print("Part 2:", sum_power)


if __name__ == "__main__":
    main()
