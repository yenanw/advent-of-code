package main

import (
	"fmt"
	"math"
	"os"
	"sort"
	"strconv"
	"strings"
)

func checkError(e error) {
	if e != nil {
		panic(e)
	}
}

func main() {
	data, err := os.ReadFile("input.txt")
	checkError(err)

	var left, right []int
	lines := strings.Split(string(data), "\n")

	// all that work to parse this file, there gotta be a better way
	for _, line := range lines {
		s := strings.Fields(line)

		l, err := strconv.Atoi(s[0])
		checkError(err)
		r, err := strconv.Atoi(s[1])
		checkError(err)

		left = append(left, l)
		right = append(right, r)
	}

	//==========PART 1==========
	sort.Ints(left)
	sort.Ints(right)

	diff := 0
	for i := 0; i < len(left); i++ {
		diff += int(math.Abs(float64(left[i] - right[i])))
	}

	fmt.Printf("Part 1: %d\n", diff)

	//==========PART 2==========
	counter := make(map[int]int)
	for _, num := range right {
		counter[num] = counter[num] + 1
	}

	acc := 0
	for _, num := range left {
		acc += num * counter[num]
	}

	fmt.Printf("Part 2: %d\n", acc)
}
