import sys
import parse as prs
import pprint as pp


def parse():
    sensors = {}
    with open("input.txt") as f:
        for line in f:
            sx, sy, cx, cy = prs.parse(
                "Sensor at x={:d}, y={:d}: closest beacon is at x={:d}, y={:d}",
                line.strip(),
            )
            sensors[(sx, sy)] = ((cx, cy), abs(sx - cx) + abs(sy - cy))
    return sensors


def no_beacon_land(sensors, row, exclude_beacon=True):
    intervals = set()
    for s in sensors:
        (sx, sy) = s
        (cx, cy), dist = sensors[s]

        dy = abs(row - sy)
        if dy > dist:
            continue

        dx = dist - dy
        start, end = sx - dx, sx + dx

        # for part 1 you need to exclude beacon to get the right answer,
        # but honestly you can just try again with (sol-1) so this is really
        # unnecessary...
        if cy == row and exclude_beacon:
            if cx == start == end:
                continue
            elif cx == start:
                intervals.add((start + 1, end))
            elif cx == end:
                intervals.add((start, end - 1))
            else:
                intervals.add((start, cx - 1))
                intervals.add((cx + 1, end))
        else:
            intervals.add((start, end))

    return merge_intervals(list(sorted(intervals)))


def count_intervals(ins):
    counter = 0
    for (s, e) in ins:
        counter += e - s + 1
    return counter


def merge_intervals(ins):
    intervals = set()
    start, end = ins[0]
    for i in range(1, len(ins)):
        (s, e) = ins[i]
        if e <= end:
            # current interval is inside the previous interval
            continue

        if s > end:
            # current interval does not overlap with previous interval
            intervals.add((start, end))
            start = s
            end = e
            continue
        # else it must overlaps and is not completely within the previous interval
        end = e

    intervals.add((start, end))
    return intervals


def find_distress(sensors, max_x, max_y):
    # just loop through 4 million rows, no biggie xd
    for row in range(0, max_y + 1):
        ins = no_beacon_land(sensors, row, exclude_beacon=False)
        if len(ins) > 1:
            ins = list(sorted(ins))
            start = ins[0][1] + 1
            end = ins[1][0] - 1

            if start == end and 0 <= start <= max_x:
                # assuming the input is always ideal so that whenever there
                # is a gap in the intervals then it's the interval we are
                # looking for
                return (start, row)
    # hoepfully this won't happen
    raise ValueError("either my solution is dog or the input is malformed")


def solve():
    sensors = parse()

    part1 = count_intervals(no_beacon_land(sensors, 2000000))

    (x, y) = find_distress(sensors, 4_000_000, 4_000_000)
    part2 = x * 4_000_000 + y
    return {"part 1": part1, "part 2": part2}


pp.pprint(solve())
